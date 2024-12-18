package faang.school.analytics.listenerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestedEventListenerTest {
    @InjectMocks
    private MentorshipRequestedEventListener listener;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private ObjectMapper objectMapper;

    private final Long testMenteeId = 1L;
    private final Long testMentorId = 2L;

    @Test
    public void testValidEvent() throws IOException {
        MentorshipRequestedEvent event = getMentorshipRequestEvent();
        AnalyticsEventDto analyticsEventDto = getAnalyticsEventDto();
        ObjectMapper realObjectMapper = new ObjectMapper();
        byte[] body = realObjectMapper.writeValueAsBytes(event);

        Message mockMessage = mock(Message.class);
        when(mockMessage.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, MentorshipRequestedEvent.class))
                .thenReturn(event);

        listener.onMessage(mockMessage, null);

        verify(analyticsEventService, times(1)).saveEvent(analyticsEventDto);
    }

    private MentorshipRequestedEvent getMentorshipRequestEvent() {
        return MentorshipRequestedEvent.builder()
                .menteeId(testMenteeId)
                .mentorId(testMentorId)
                .build();
    }

    private AnalyticsEventDto getAnalyticsEventDto() {
        return AnalyticsEventDto.builder()
                .actorId(testMenteeId)
                .receiverId(testMentorId)
                .eventType(EventType.MENTORSHIP_REQUEST)
                .build();
    }
}
