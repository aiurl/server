package io.theurl.framework.configure;

//import org.apache.hc.client5.http.config.RequestConfig;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
//import org.apache.hc.core5.util.Timeout;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

//@Configuration
//public class RestTemplateConfig {
//    @ConditionalOnMissingBean(RestTemplate.class)
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
//        return restTemplate;
//    }
//
//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        int timeout = 5000;
//        RequestConfig config = RequestConfig.custom()
//            .setConnectionRequestTimeout(Timeout.of(timeout, TimeUnit.MILLISECONDS))
//            //.setSocketTimeout(timeout)
//            .build();
//        CloseableHttpClient client = HttpClientBuilder
//            .create()
//            .setDefaultRequestConfig(config)
//            .build();
//        return new HttpComponentsClientHttpRequestFactory(client);
//    }
//}
