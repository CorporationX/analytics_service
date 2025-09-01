package faang.school.analytics.listener;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "audit-kafka.enabled", havingValue = "true")
public class AnalyticsEventListener {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${audit-kafka.topic}",
            containerFactory = "analiticsEventListenerContainerFactory",
            groupId = "${audit-kafka.consumer.group-id}"
    )
    public void consume(@Payload EventDto analiticsEvent) {
        analyticsEventService.saveEvent(analiticsEvent);
        System.out.println(analiticsEvent);
    }
}
