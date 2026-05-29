package ru.vsu.cs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.dto.ServerEventDto;
import ru.vsu.cs.producer.Producer;

@RestController
@RequiredArgsConstructor
public class ProducerController {
    private final Producer producer;

    @PostMapping(path = "/direct/{routingKey}")
    public void sendRabbitMessage(@RequestBody ServerEventDto dto, @PathVariable String routingKey) {
        producer.sendDirectMessage(dto, routingKey);
    }
}
