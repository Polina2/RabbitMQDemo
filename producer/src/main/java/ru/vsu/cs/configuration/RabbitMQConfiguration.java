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

@Configuration
public class RabbitMQConfiguration {
    @Value("${app.rabbitmq.queue1}")
    private String queueName1;

    @Value("${app.rabbitmq.queue2}")
    private String queueName2;

    @Value("${app.rabbitmq.directExchange}")
    private String directExchangeName;

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
    Queue queue1() {
        return QueueBuilder.durable(queueName1).build();
    }

    @Bean
    Queue queue2() {
        return QueueBuilder.durable(queueName2).build();
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
