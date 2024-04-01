package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.handler.EventHandler;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEvent> implements MessageListener {

    public MentorshipRequestedEventListener(ObjectMapper objectMapper, List<EventHandler<MentorshipRequestedEvent>> eventHandlers, AnalyticsEventService analyticsEventService) {
        super(objectMapper, eventHandlers, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipRequestedEvent.class, analyticsEventService::saveMentorshipRequestedEvent);
    }
}
