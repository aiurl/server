package io.theurl.identity.interfaces.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.theurl.identity.application.contract.UserApplicationService;
import io.theurl.identity.application.dto.UserProfileResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "jwt.secret=01234567890123456789012345678901")
class AccountControllerSecurityTests {

    private static final String TEST_SECRET = "01234567890123456789012345678901";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserApplicationService userApplicationService;

    @Test
    void shouldReturn401WhenProfileRequestHasNoJwt() throws Exception {
        mockMvc.perform(get("/account/profile"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn200WhenProfileRequestHasValidJwt() throws Exception {
        when(userApplicationService.getProfileAsync()).thenReturn(CompletableFuture.completedFuture(new UserProfileResponseDto()));

        mockMvc.perform(get("/account/profile")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + createToken("1")))
               .andExpect(status().isOk());

        verify(userApplicationService).getProfileAsync();
    }

    @Test
    void shouldAllowRegisterWithoutJwt() throws Exception {
        when(userApplicationService.createAsync(any())).thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(post("/account/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"demo\",\"password\":\"pwd\"}"))
               .andExpect(status().isOk());

        verify(userApplicationService).createAsync(any());
    }

    /**
     * 生成用于测试鉴权流程的短时JWT。
     */
    private String createToken(String subject) {
        return Jwts.builder()
                   .subject(subject)
                   .issuedAt(new Date())
                   .expiration(Date.from(Instant.now().plusSeconds(300)))
                   .signWith(Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    }
}

