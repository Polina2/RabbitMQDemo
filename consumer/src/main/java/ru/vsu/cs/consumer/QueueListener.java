package ru.vsu.cs.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.vsu.cs.dto.ServerEventDto;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${app.rabbitmq.queue}")
public class QueueListener {
    @RabbitHandler
    public void receiver(ServerEventDto serverEventDto) {
        System.out.printf(
                "severity: %s, server: %s, location: %s",
                serverEventDto.severity(),
                serverEventDto.server(),
                serverEventDto.location()
        );
    }
}
