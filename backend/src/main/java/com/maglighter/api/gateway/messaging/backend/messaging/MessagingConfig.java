package com.maglighter.api.gateway.messaging.backend.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessagingConfig {

    public static final String ERROR_CHANNEL = "errorChannel";

    @Value("${application.messaging.rpc-topic-exchange}")
    private String rpcTopicExchange;

    @Value("${application.messaging.request-queue}")
    private String requestQueue;

    @Value("${application.messaging.request-routing-key}")
    private String requestRoutingKey;

    @Bean
    public Queue requestsQueue() {
        return QueueBuilder
            .durable(requestQueue)
            .build();
    }

    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder
            .topicExchange(rpcTopicExchange)
            .build();
    }

    @Bean
    public Binding requestsBinding(TopicExchange topicExchange) {
        return BindingBuilder.bind(requestsQueue())
                             .to(topicExchange)
                             .with(requestRoutingKey);
    }

    @Bean
    public MessageChannel requestChannel() {
        return MessageChannels.queue(requestQueue)
                              .get();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SerializerMessageConverter();
    }

    @Bean
    public Jackson2JsonObjectMapper jsonObjectMapper(ObjectMapper objectMapper) {
        return new Jackson2JsonObjectMapper(objectMapper);
    }

    @Bean
    public IntegrationFlow inboundFlow(
        ConnectionFactory rabbitConnectionFactory, Jackson2JsonObjectMapper jsonObjectMapper
    ) {
        return IntegrationFlows.from(Amqp.inboundGateway(rabbitConnectionFactory, requestQueue)
                                         .messageConverter(messageConverter())
                                         .errorChannel(ERROR_CHANNEL))
                               .transform(Transformers.fromJson(jsonObjectMapper))
                               .log()
                               .gateway(routing())
                               .logAndReply();
    }

//    @Bean
//    public IntegrationFlow exceptionFlow() {
//        return IntegrationFlows.from(ERROR_CHANNEL)
//                .handle("messagingExceptionHandler", "handle")
//                .get();
//    }

    @Bean
    public IntegrationFlow routing() {
        return f -> f.route("headers['calledMethod']");
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new PageModule());
        objectMapper.registerModule(new SpringDataModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        return objectMapper;
    }
}
