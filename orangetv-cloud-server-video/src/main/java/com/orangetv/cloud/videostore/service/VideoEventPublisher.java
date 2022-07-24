package com.orangetv.cloud.videostore.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoEventPublisher {

    private final NewTopic videoMetadataTopic;
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public void onPosterGenerated(Integer videoId, String info) {
        kafkaTemplate.send(videoMetadataTopic.name(), videoId, info);
    }
}
