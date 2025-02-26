package faang.school.analytics.listeners;


import faang.school.analytics.dto.RecommendationAnalyticDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationAnalyticListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @KafkaListener(topics = "${topic.recommendation-topic}",
    containerFactory = "kafkaListenerContainerFactory")
    public void recommendationAnalytic(RecommendationAnalyticDto recommendationAnalyticDto) {
        AnalyticsEvent event = analyticsEventMapper.toEntityForRecomendation(recommendationAnalyticDto);
        event.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventService.saveEvent(event);
    }


}
