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
    public void sendDirectMessage(@RequestBody ServerEventDto dto, @PathVariable String routingKey) {
        producer.sendDirectMessage(dto, routingKey);
    }

    @PostMapping(path = "/fanout")
    public void sendFanoutMessage(@RequestBody ServerEventDto dto) {
        producer.sendFanoutMessage(dto);
    }

    @PostMapping(path = "/topic/{routingKey}")
    public void sendTopicMessage(@RequestBody ServerEventDto dto, @PathVariable String routingKey) {
        producer.sendTopicMessage(dto, routingKey);
    }

    @PostMapping(path = "/headers")
    public void sendHeadersMessage(@RequestBody ServerEventDto dto) {
        producer.sendHeadersMessage(dto);
    }

    @PostMapping(path = "/dead")
    public void sendDeadMessage(@RequestBody ServerEventDto dto) {
        producer.sendDeadMessage(dto);
    }
}
