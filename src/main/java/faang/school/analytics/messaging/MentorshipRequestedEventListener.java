package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.client.UserServiceClient;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestedEvent mentorshipRequestedEvent = objectMapper.readValue(message.getBody(),
                    MentorshipRequestedEvent.class);
            String receiverName = userServiceClient.getById(mentorshipRequestedEvent.receiverId()).username();
            String actorName = userServiceClient.getById(mentorshipRequestedEvent.actorId()).username();
            log.debug("Mentorship Request Event (receiver={}, actor={}) received from User Service.",
                    receiverName, actorName);
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(mentorshipRequestedEvent));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't parse message into MentorshipRequestedEvent");
        }
    }
}
