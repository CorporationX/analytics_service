package faang.school.analytics.kafka.consumer;

import faang.school.analytics.kafka.events.RecommendationEvent;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener {
    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventService service;

    @KafkaListener(
            topics = "${spring.kafka.topics.recommendation-events.name}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(RecommendationEvent event){
        log.info("Received RecommendationEvent: {}", event);
        try {
            var entity = mapper.fromEvent(event);
            service.save(entity);
            log.info("Saved AnalyticsEvent for recommendationId={}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process RecommendationEvent: {}", event, e);
            throw e;
        }
    }
}
