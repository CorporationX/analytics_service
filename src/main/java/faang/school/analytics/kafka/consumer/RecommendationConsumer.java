package faang.school.analytics.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.kafka.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationConsumer {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "new-recommendation",
            groupId = "new-recommendation-group",
            containerFactory = "objectContainerFactory"
    )
    public void consumeEvent(ConsumerRecord<String, Object> consumerRecord) {
        RecommendationEvent recommendationEvent = objectMapper.convertValue(
                consumerRecord.value(), RecommendationEvent.class);
        log.info("Получен новый RecommendationEvent c recommendationId: {} из Kafka.", recommendationEvent.recommendationId());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setReceiverId(recommendationEvent.receiverId());
        analyticsEvent.setAuthorId(recommendationEvent.authorId());
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Новый RecommendationEvent c recommendationId: {} сохранен в бд.", recommendationEvent.recommendationId());
    }
}

