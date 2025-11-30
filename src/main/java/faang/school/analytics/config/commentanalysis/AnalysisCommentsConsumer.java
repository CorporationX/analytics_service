package faang.school.analytics.config.commentanalysis;

import faang.school.analytics.dto.commentanalysis.AnalysisCommentsEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisCommentsConsumer {
    @Autowired
    private AnalyticsEventService analyticsService;

    @Value("${spring.topic.analytics}")
    private String topicName;

    @KafkaListener(topics = "${spring.topic.analytics}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(@Payload AnalysisCommentsEventDto event) {
        try {
            log.info("Received Analysis Comments Event from Post Service: {}", event);
            analyticsService.handleEvent(event); // Просто передаём событие в сервис
        } catch (Exception e) {
            log.error("Error processing Analysis Comments Event: ", e);
        }
    }
}