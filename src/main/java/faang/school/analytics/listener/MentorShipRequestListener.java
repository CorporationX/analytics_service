package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorShipRequestListener implements MessageListener {

    private final ObjectMapper mapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestedEvent mentorshipRequestedEvent = mapper.readValue(message.getBody(), MentorshipRequestedEvent.class);
            AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                    .actorId(mentorshipRequestedEvent.getRequesterUserId())
                    .receiverId(mentorshipRequestedEvent.getReceiverUserId())
                    .receivedAt(mentorshipRequestedEvent.getRequestedTime())
                    .eventType(EventType.PROJECT_INVITE)
                    .build();
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (IOException e) {
            log.error("Can`t read message from redis {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
