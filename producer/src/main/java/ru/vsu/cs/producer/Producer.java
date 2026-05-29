package ru.vsu.cs.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vsu.cs.dto.ServerEventDto;

@Service
@RequiredArgsConstructor
public class Producer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.directExchange}")
    private String directExchange;

    public void sendDirectMessage(ServerEventDto serverEventDto, String routingKey) {
        rabbitTemplate.convertAndSend(directExchange, routingKey, serverEventDto);
    }
}
