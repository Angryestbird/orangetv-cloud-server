package com.orangetv.cloud.videostore.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final NewTopic SystemInfoTopic;
    private final NewTopic videoMetadataTopic;

    private final KafkaTemplate<Long, String> kafkaTemplate;

    public void publishMetadata(Integer videoId, String info) {
        kafkaTemplate.send(videoMetadataTopic.name(), (long) videoId, info);
    }

    public void publishSysInfo(long epoch, String info) {
        kafkaTemplate.send(SystemInfoTopic.name(), epoch, info);
    }
}
