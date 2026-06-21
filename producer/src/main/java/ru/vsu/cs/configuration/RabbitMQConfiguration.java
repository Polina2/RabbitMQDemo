package ru.vsu.cs.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {
    @Value("${app.rabbitmq.queue1}")
    private String queueName1;

    @Value("${app.rabbitmq.queue2}")
    private String queueName2;

    @Value("${app.rabbitmq.directExchange}")
    private String directExchangeName;

    @Value("${app.rabbitmq.fanoutExchange}")
    private String fanoutExchangeName;

    @Value("${app.rabbitmq.topicExchange}")
    private String topicExchangeName;

    @Value("${app.rabbitmq.headersExchange}")
    private String headersExchangeName;

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(headersExchangeName);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public Binding binding1(Queue queue1, DirectExchange directExchange, @Value("${app.rabbitmq.routingKey1}") String routingKey){
        return BindingBuilder.bind(queue1).to(directExchange).with(routingKey);
    }

    @Bean
    public Binding binding2(Queue queue2, DirectExchange directExchange, @Value("${app.rabbitmq.routingKey2}") String routingKey){
        return BindingBuilder.bind(queue2).to(directExchange).with(routingKey);
    }

    @Bean
    public Binding binding3(Queue queue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }

    @Bean
    public Binding binding4(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    @Bean
    public Binding binding5(Queue queue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue1).to(topicExchange).with("*.srv1.*");
    }

    @Bean
    public Binding binding6(Queue queue2, HeadersExchange headersExchange) {
        Map<String, Object> map = new HashMap<>();
        map.put("location", "spb1");
        map.put("severity", "info");
        return BindingBuilder.bind(queue2).to(headersExchange).whereAll(map).match();
    }

    @Bean
    Queue queue1() {
        return QueueBuilder.durable(queueName1).build();
    }

    @Bean
    Queue queue2() {
        return QueueBuilder.durable(queueName2).build();
    }

    @Bean
    public Queue classicQueue() {
        return QueueBuilder.durable("classic.queue")
                .withArguments(Map.of("x-queue-type", "classic"))
                .build();
    }

    @Bean
    public Queue quorumQueue() {
        return QueueBuilder.durable("quorum.queue")
                .withArguments(Map.of("x-queue-type", "quorum"))
                .build();
    }

    @Bean
    public Queue streamQueue() {
        return QueueBuilder.durable("stream.queue")
                .withArguments(Map.of("x-queue-type", "stream"))
                .build();
    }

    // Настройка DLX для очереди
    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dlx.exchange");
        args.put("x-dead-letter-routing-key", "failed");
        args.put("x-message-ttl", 60000); // 1 минута

        return QueueBuilder.durable("main.queue")
                .withArguments(args)
                .build();
    }

    // DLX Exchange и очередь
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx.exchange");
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable("dlq.queue").build();
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlqQueue)
                .to(dlxExchange)
                .with("failed");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }
}
