package faang.school.analytics.messaging.listener.kafka;

import faang.school.analytics.messaging.event.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileViewEventListener {

    private final AnalyticsEventService analyticsEventService;
    @Value("${spring.data.kafka.port}")
    private String port;

    @Value("${spring.data.kafka.host}")
    private String host;


    @KafkaListener(topics = "${spring.data.kafka.channel.profile-view}", groupId = "{spring.data.kafka.group-id}")
    public void listen(ProfileViewEvent message, Acknowledgment acknowledgment) {
        log.info("event from kafka :" + message);
        analyticsEventService.saveProfileViewEvent(message);
        acknowledgment.acknowledge();
    }
}
