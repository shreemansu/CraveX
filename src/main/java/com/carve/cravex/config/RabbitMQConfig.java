package com.carve.cravex.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

//  Exchange
    public static final String EMAIL_EXCHANGE="email.exchange";

//  Queues
    public static final String EMAIL_OTP_QUEUE="email.otp.queue";
    public static final String EMAIL_WELCOME_QUEUE="email.welcome.queue";
    public static final String EMAIL_ORDER_QUEUE="email.order.queue";
    public static final String EMAIL_DELIVERY_QUEUE="email.delivery.queue";

//  Routing Keys
    public static final String EMAIL_OTP_ROUTING_KEY="email.otp.send";
    public static final String EMAIL_WELCOME_ROUTING_KEY="email.welcome.send";
    public static final String EMAIL_ORDER_ROUTING_KEY ="email.order.send";
    public static final String EMAIL_DELIVERY_ROUTING_KEY="email.delivery.send";

    @Bean
    public Queue emailOtpQueue(){
        return QueueBuilder.durable(EMAIL_OTP_QUEUE).build();
    }

    @Bean
    public Queue emailWelcomeQueue(){return QueueBuilder.durable(EMAIL_WELCOME_QUEUE).build();}

    @Bean
    public Queue emailOrderQueue(){return QueueBuilder.durable(EMAIL_ORDER_QUEUE).build();}

    @Bean
    public Queue emailDeliveryQueue(){return QueueBuilder.durable(EMAIL_DELIVERY_QUEUE).build();}

    @Bean
    public DirectExchange emailExchange(){
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailOtpBinding(){
        return BindingBuilder.bind(emailOtpQueue())
                .to(emailExchange())
                .with(EMAIL_OTP_ROUTING_KEY);
    }

    @Bean
    public Binding emailWelcomeBinding(){
        return BindingBuilder.bind(emailWelcomeQueue())
                .to(emailExchange())
                .with(EMAIL_WELCOME_ROUTING_KEY);
    }

    @Bean
    public Binding emailOrderBinding(){
        return BindingBuilder.bind(emailOrderQueue())
                .to(emailExchange())
                .with(EMAIL_ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding emailDeliveryBinding(){
        return BindingBuilder.bind(emailDeliveryQueue())
                .to(emailExchange())
                .with(EMAIL_DELIVERY_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
