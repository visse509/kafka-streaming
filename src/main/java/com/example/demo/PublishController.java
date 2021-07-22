package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class PublishController {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping(path = "/publish/")
    void publish() {
        kafkaTemplate.send("in-topic", "" + Instant.now());
    }
}
