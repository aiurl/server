package io.theurl.identity.application.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import io.theurl.identity.domain.event.OnetimePasswordCreatedEvent;
import io.theurl.identity.domain.event.UserEmailChangedEvent;
import io.theurl.identity.domain.event.UserPasswordChangedEvent;
import io.theurl.identity.domain.event.UserPhoneChangedEvent;
import io.theurl.shared.event.OnetimePasswordCreatedEto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DistributedEventBus {
    private final Logger LOGGER = LoggerFactory.getLogger(DistributedEventBus.class);

    private final ModelMapper mapper;

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    private final ConnectionFactory factory;

    public DistributedEventBus(ModelMapper mapper) {
        this.mapper = mapper;
        this.factory = new ConnectionFactory();
        this.factory.setHost(host);
        this.factory.setPort(port);
        this.factory.setUsername(username);
        this.factory.setPassword(password);
    }

    @Async
    @EventListener
    public void handleOnetimePasswordCreatedEvent(OnetimePasswordCreatedEvent event) {
        try (var connection = factory.newConnection()) {
            var eto = mapper.map(event, OnetimePasswordCreatedEto.class);

            var message = new ObjectMapper().writeValueAsString(eto);

            var channel = connection.createChannel();
            channel.exchangeDeclare("io.theurl.identity.otp.created", "fanout", true);
            var properties = new AMQP.BasicProperties().builder()
                                                       .contentType("application/json")
                                                       .build();
            channel.basicPublish("io.theurl.identity.otp.created", "", properties, message.getBytes());

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleUserEmailChangedEvent(UserEmailChangedEvent event) {
        try {

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleUserPasswordChangedEvent(UserPasswordChangedEvent event) {

    }

    @Async
    @EventListener
    public void handleUserPhoneChangedEvent(UserPhoneChangedEvent event) {

    }
}
