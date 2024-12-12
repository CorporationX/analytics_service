package faang.school.analytics.listener.mentorshiprequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.analytic.mentorshiprequest.MentorshipRequestedEvent;

import faang.school.analytics.mapper.mentorshiprequest.MentorshipRequestEventMapperImpl;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private MentorshipRequestEventMapperImpl mapper;
    @InjectMocks
    MentorshipRequestedEventListener eventListener;

    @Test
    public void testOnMessagePositiveTest() throws IOException {
        MentorshipRequestedEvent event = prepareEvent();
        AnalyticsEventDto mappedEvent = mapper.mentorshipRequestedToAnalyticsDto(event);

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);
        when(analyticsEventService.saveEvent(mappedEvent)).thenReturn(mappedEvent);

        eventListener.onMessage(message, messageBody);

        verify(analyticsEventService).saveEvent(mappedEvent);
    }

    @Test
    public void testOnMessageExceptionTest() throws IOException {
        MentorshipRequestedEvent event = prepareEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEvent.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(RuntimeException.class, () -> eventListener.onMessage(message, messageBody));
    }

    public MentorshipRequestedEvent prepareEvent() {
        return new MentorshipRequestedEvent(1L, 1L, 1L, LocalDateTime.now());
    }
}
