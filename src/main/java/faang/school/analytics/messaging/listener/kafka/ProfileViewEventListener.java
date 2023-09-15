package faang.school.analytics.messaging.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.messaging.event.ProfileViewEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {

    @Autowired
    public ProfileViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @KafkaListener(topics = "profile_view_channel", groupId = "profile_view")
    public void listen(String message) {
        ProfileViewEvent event = eventHandler(message, ProfileViewEvent.class);
        log.info("event from kafka :" + message);
        analyticsEventService.saveProfileViewEvent(event);
    }
}
