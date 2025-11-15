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
public class ProfileViewEventListener {

    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(
            topics = "${kafka.consumers.profile-view.topic}",
            containerFactory = "profileViewEventKafkaListenerContainerFactory"
    )
    public void listen(
            ProfileViewEvent event,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        log.info("Received ProfileViewEvent: userId={}, viewerId={}, partition={}, topic={}",
                event.userId(), event.viewerId(), partition, topic);

        try {
            analyticsEventService.processProfileViewEvent(event);
            acknowledgment.acknowledge();

            log.info("Successfully acknowledged ProfileViewEvent: userId={}, partition={}",
                    event.userId(), partition);
        } catch (Exception e) {
            log.error("Error processing ProfileViewEvent: userId={}, viewerId={}, partition={}, error={}",
                    event.userId(), event.viewerId(), partition, e.getMessage(), e);
        }
    }
}

