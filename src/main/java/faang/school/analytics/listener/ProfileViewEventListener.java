package faang.school.analytics.listener;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements AnalyticsEventListener<ProfileViewEvent> {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${kafka.consumers.profile-view.topic}",
            containerFactory = "profileViewEventKafkaListenerContainerFactory"
    )
    public void listen(ProfileViewEvent event,
                       Acknowledgment acknowledgment,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Received ProfileViewEvent: partition={}, topic={}, event={}", partition, topic, event);

        try {
            processEvent(event);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing ProfileViewEvent: partition={}, error={}", partition, e.getMessage(), e);
        }
    }

    @Override
    public void processEvent(ProfileViewEvent event) {
        analyticsEventService.processProfileViewEvent(event);
    }
}