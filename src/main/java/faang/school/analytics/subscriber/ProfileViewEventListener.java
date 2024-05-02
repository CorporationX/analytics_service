package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    public ProfileViewEventListener(ObjectMapper objectMapper, AnalyticsMapper<ProfileViewEvent, AnalyticsEvent> mapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, mapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AnalyticsEvent analyticsEvent = readAndMapEvent(message, ProfileViewEvent.class);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        log.info("Mapped event to analyticsEvent: {}", analyticsEvent);
        saveEventAnalytics(analyticsEvent);
    }
}
