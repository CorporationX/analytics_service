package faang.school.analytics.listener;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.MentorshipRequestedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorshipRequestedEventListener implements MessageListener {
    private final JsonObjectMapper jsonObjectMapper;
    private final MentorshipRequestedEventService mentorshipRequestedEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEventDto eventDto = jsonObjectMapper.readValue(message.getBody(), MentorshipRequestedEventDto.class);
        eventDto.setEventType(EventType.MENTORSHIP_REQUEST);
        mentorshipRequestedEventService.saveEvent(eventDto);
    }
}
