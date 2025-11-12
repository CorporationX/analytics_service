package faang.school.analytics.listener;

import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    public void listen(ProfileViewEvent event, Acknowledgment acknowledgment) {
        try {
            analyticsEventService.processProfileViewEvent(event);
            acknowledgment.acknowledge();

            log.info("Successfully acknowledged ProfileViewEvent for userId={}", event.userId());
        } catch (Exception e) {
            log.error("Error processing ProfileViewEvent: userId={}, viewerId={}, error={}",
                    event.userId(), event.viewerId(), e.getMessage(), e);
        }
    }
}

