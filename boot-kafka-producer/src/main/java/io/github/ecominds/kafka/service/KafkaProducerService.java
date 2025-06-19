package io.github.ecominds.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaProducerService {
    private final AdminClient adminClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(AdminClient adminClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.adminClient = adminClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Set<String> listAllTopics() {
        try {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false); // Set true to include internal topics
            return adminClient.listTopics(options).names().get();
        }catch (Exception ex){
            log.error("Kafka producer list all topics failed", ex);
        }
        return Collections.emptySet();
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}