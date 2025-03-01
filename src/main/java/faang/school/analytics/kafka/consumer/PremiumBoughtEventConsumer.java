package faang.school.analytics.kafka.consumer;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.kafka.events.PremiumBoughtEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventConsumer {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.premium-bought}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(PremiumBoughtEvent event) {
        log.info("Received event: {}", event);

        AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                .receiverId(event.getUserId())
                .eventType(EventType.PREMIUM_BOUGHT)
                .receivedAt(event.getBoughtAt())
                .build();

        analyticsEventService.saveEvent(analyticsEventDto);
    }
}
