package ru.vsu.cs.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.vsu.cs.dto.ServerEventDto;

@Component
@RequiredArgsConstructor
public class QueueListener {

    @RabbitListener(queues = "${app.rabbitmq.queue1}")
    public void receiver1(ServerEventDto serverEventDto) {
        System.out.printf(
                "severity: %s, server: %s, location: %s, queue1\n",
                serverEventDto.severity(),
                serverEventDto.server(),
                serverEventDto.location()
        );
    }

    @RabbitListener(queues = "${app.rabbitmq.queue2}")
    public void receiver2(ServerEventDto serverEventDto) {
        System.out.printf(
                "severity: %s, server: %s, location: %s, queue2\n",
                serverEventDto.severity(),
                serverEventDto.server(),
                serverEventDto.location()
        );
    }

    // Обработчик "мертвых" писем
    @RabbitListener(queues = "dlq.queue")
    public void handleDeadLetter(Message message) {
        System.out.println("Handle dead letter");
        // Логирование, алерт, ручная обработка
    }
}
