package io.theurl.identity.application.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import io.theurl.identity.domain.aggregate.User;
import io.theurl.identity.domain.event.*;
import io.theurl.shared.constant.EventConstant;
import io.theurl.shared.event.OnetimePasswordCreatedEto;
import io.theurl.shared.event.UserLockedEto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DistributedEventPublisher {
    private final Logger LOGGER = LoggerFactory.getLogger(DistributedEventPublisher.class);

    private final ModelMapper mapper;

    private final ConnectionFactory factory;

    public DistributedEventPublisher(ModelMapper mapper, ConnectionFactory factory) {
        this.mapper = mapper;
        this.factory = factory;
    }

    @Async
    @EventListener
    public void handleOnetimePasswordCreatedEvent(OnetimePasswordCreatedEvent event) {
        try (var connection = factory.newConnection()) {
            var eto = mapper.map(event, OnetimePasswordCreatedEto.class);

            var message = new ObjectMapper().writeValueAsString(eto);

            var channel = connection.createChannel();
            channel.exchangeDeclare(EventConstant.OTP_CREATED, "fanout", true);
            var properties = new AMQP.BasicProperties().builder()
                                                       .contentType("application/json")
                                                       .build();
            channel.basicPublish(EventConstant.OTP_CREATED, "", properties, message.getBytes());

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    @Async
    @EventListener
    public void handleUserEmailChangedEvent(UserEmailChangedEvent event) {
        LOGGER.debug("收到用户邮箱变更事件，当前尚未接入分布式发布：{}", event.getClass().getSimpleName());
    }

    @Async
    @EventListener
    public void handleUserPasswordChangedEvent(UserPasswordChangedEvent event) {
        LOGGER.debug("收到用户密码变更事件，当前尚未接入分布式发布：{}", event.getClass().getSimpleName());
    }

    @Async
    @EventListener
    public void handleUserPhoneChangedEvent(UserPhoneChangedEvent event) {
        LOGGER.debug("收到用户手机号变更事件，当前尚未接入分布式发布：{}", event.getClass().getSimpleName());
    }

    @Async
    @EventListener
    public void handleUserLockedEvent(UserLockedEvent event) {
        try (var connection = factory.newConnection()) {
            var eto = mapper.map(event, UserLockedEto.class);

            var aggregate = event.getAggregate(User.class);
            if (aggregate != null) {
                eto.setPhone(aggregate.getPhone());
                eto.setEmail(aggregate.getEmail());
                eto.setUsername(aggregate.getUsername());
            }

            var message = new ObjectMapper().writeValueAsString(eto);

            var channel = connection.createChannel();
            channel.exchangeDeclare(EventConstant.USER_LOCKED, "fanout", true);
            var properties = new AMQP.BasicProperties().builder()
                                                       .contentType("application/json")
                                                       .build();
            channel.basicPublish(EventConstant.USER_LOCKED, "", properties, message.getBytes());

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }
}
