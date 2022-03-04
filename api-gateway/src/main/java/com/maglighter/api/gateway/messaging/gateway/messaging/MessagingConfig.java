package com.maglighter.api.gateway.messaging.gateway.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import org.springframework.messaging.MessageChannel;

@Configuration
public class MessagingConfig {

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
    public MessageChannel inputChannel() {
        return MessageChannels.direct()
                              .get();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SerializerMessageConverter();
    }

    @Bean
    public IntegrationFlow outboundFlow(RabbitTemplate rabbitTemplate) {
        return IntegrationFlows.from(inputChannel())
                               .transform(Transformers.toJson())
                               .log()
                               .handle(Amqp.outboundGateway(rabbitTemplate)
                                           .routingKey(requestRoutingKey)
                                           .exchangeName(rpcTopicExchange))
                               .logAndReply();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        return objectMapper;
    }
}