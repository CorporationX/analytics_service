package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.RecomendationCreateEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecomendationEventListener {
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.recomendation-create}",
            properties = "spring.json.value.default.type=faang.school.analytics.dto.RecomendationCreateEvent",
            groupId = "${spring.kafka.group-id}"
    )
    public void onRecomendationCreate (RecomendationCreateEvent event) {
        sendEvent(event.authorId(), event.recipientId(), EventType.RECOMMENDATION_RECEIVED);
    }

    private void sendEvent(Long actorId, Long receiverId, EventType type) {
        AnalyticsEventDto analyticsEven = AnalyticsEventDto.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .eventType(type)
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEventService.saveEvent(analyticsEven);
    }
}
