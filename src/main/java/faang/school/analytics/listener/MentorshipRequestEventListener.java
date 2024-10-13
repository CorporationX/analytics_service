package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.message.MentorshipRequestMessage;
import faang.school.analytics.mapper.MentorshipRequestMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class MentorshipRequestEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private MentorshipRequestMapper mentorshipRequestMapper;
    private AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestMessage mentorshipRequestMessage = objectMapper.readValue(message.getBody(),
                    MentorshipRequestMessage.class);
            AnalyticsEvent analyticsEvent = mentorshipRequestMapper.toAnalyticsEvent(mentorshipRequestMessage);
            analyticsEvent.setEventType(EventType.of(16));
            analyticsEventService.SaveAnalyticsEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
