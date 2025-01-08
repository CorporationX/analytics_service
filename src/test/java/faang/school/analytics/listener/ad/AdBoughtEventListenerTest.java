package faang.school.analytics.listener.ad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.mentorshiprequest.MentorshipRequestedEvent;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdBoughtEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private AdBoughtEventListener adBoughtEventListener;

    @Test
    void testOnMessagePositiveTest() throws IOException {
        AnalyticsEventDto analyticsEvent = new AnalyticsEventDto();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(analyticsEvent);
        when(objectMapper.readValue(message.getBody(), AnalyticsEventDto.class)).thenReturn(analyticsEvent);
        when(message.getBody()).thenReturn(messageBody);
        when(analyticsEventService.saveEvent(analyticsEvent)).thenReturn(analyticsEvent);

        adBoughtEventListener.onMessage(message, messageBody);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessageExceptionTest() throws IOException {
        AnalyticsEventDto analyticsEvent = new AnalyticsEventDto();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(analyticsEvent);
        when(objectMapper.readValue(message.getBody(), AnalyticsEventDto.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(IllegalStateException.class, () -> adBoughtEventListener.onMessage(message, messageBody));
    }
}