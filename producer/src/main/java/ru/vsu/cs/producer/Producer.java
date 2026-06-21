package ru.vsu.cs.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vsu.cs.dto.ServerEventDto;

@Service
@RequiredArgsConstructor
public class Producer {
    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter converter;

    @Value("${app.rabbitmq.directExchange}")
    private String directExchange;

    @Value("${app.rabbitmq.fanoutExchange}")
    private String fanoutExchange;

    @Value("${app.rabbitmq.topicExchange}")
    private String topicExchange;

    @Value("${app.rabbitmq.headersExchange}")
    private String headersExchange;

    public void sendDirectMessage(ServerEventDto serverEventDto, String routingKey) {
        rabbitTemplate.convertAndSend(directExchange, routingKey, serverEventDto);
    }

    public void sendFanoutMessage(ServerEventDto serverEventDto) {
        rabbitTemplate.convertAndSend(fanoutExchange, serverEventDto);
    }

    public void sendTopicMessage(ServerEventDto serverEventDto, String routingKey) {
        rabbitTemplate.convertAndSend(topicExchange, routingKey, serverEventDto);
        //routing key = info.srv1.spb1
    }

    public void sendHeadersMessage(ServerEventDto serverEventDto) {
        MessageProperties props = new MessageProperties();
        props.setHeader("location", "spb1");
        props.setHeader("severity", "info");
        Message message = converter.toMessage(serverEventDto, props);
        rabbitTemplate.send(headersExchange, "", message);
    }

    public void sendDeadMessage(ServerEventDto dto) {
        rabbitTemplate.convertAndSend("", "main.queue", dto);
    }
}
