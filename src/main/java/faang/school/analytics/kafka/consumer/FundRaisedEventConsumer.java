package faang.school.analytics.kafka.consumer;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.kafka.events.FundRaisedEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundRaisedEventConsumer {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.fundraise}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(FundRaisedEvent event) {
        log.info("Received event: {}", event);

        AnalyticsEventDto saveEvent = AnalyticsEventDto.builder()
                .receiverId(event.getProjectId())
                .actorId(event.getUserId())
                .eventType(EventType.DONATION_RECEIVED)
                .receivedAt(event.getCreatedAt())
                .build();

        analyticsEventService.saveEvent(saveEvent);
    }
}
