package io.github.ecominds.kafka.controller;

import io.github.ecominds.kafka.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppControllerV1 {

    @Autowired
    private KafkaProducerService producerService;

    @RequestMapping("get")
    public ResponseEntity<?> get() {
        log.info("get request received");
        return ResponseEntity.ok(producerService.listAllTopics());
    }

    @RequestMapping("send")
    public ResponseEntity<?> send(String topic, String message) {
        log.info("request received to send message to topic {}", topic);
        producerService.sendMessage(topic, message);
        return ResponseEntity.ok("message sent to topic " + topic);
    }

}