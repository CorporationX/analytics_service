package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MentorshipRequestListener mentorshipRequestListener;

    private Message message;
    private byte[] nameChannel;
    private byte[] messageBytes;
    private MentorshipRequestEvent expectedEvent;

    @BeforeEach
    public void setUp() {
        String json = "{\"requesterId\": 12345, \"receiverId\": 67890, \"requestTime\": \"2024-10-12T10:00:00\"}";

        message = mock(Message.class);
        expectedEvent = new MentorshipRequestEvent();
        expectedEvent.setRequesterId(12345L);
        expectedEvent.setReceiverId(67890L);
        expectedEvent.setRequestTime(LocalDateTime.parse("2024-10-12T10:00:00"));

        nameChannel = "nameChannel".getBytes();
        messageBytes = json.getBytes();
    }

    @Test
    public void testOnMessage() throws IOException {
        when(objectMapper.readValue(messageBytes, MentorshipRequestEvent.class)).thenReturn(expectedEvent);
        when(message.getBody()).thenReturn(messageBytes);
        when(message.getChannel()).thenReturn(nameChannel);

        mentorshipRequestListener.onMessage(message, null);

        ArgumentCaptor<MentorshipRequestEvent> eventCaptor = ArgumentCaptor.forClass(MentorshipRequestEvent.class);
        verify(analyticsEventService).saveAnalyticEvent(eventCaptor.capture());
        assertEquals(expectedEvent, eventCaptor.getValue());
    }

    @Test
    public void testOnMessageException() throws IOException {
        String invalidJson = "{ \"invalidField\": }";
        byte[] invalidJsonBytes = invalidJson.getBytes();
        when(message.getBody()).thenReturn(invalidJsonBytes);
        when(message.getChannel()).thenReturn(nameChannel);
        when(objectMapper.readValue(invalidJsonBytes, MentorshipRequestEvent.class)).thenThrow(new IOException("Invalid JSON"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> mentorshipRequestListener.onMessage(message, null));

        assertTrue(thrown.getCause() instanceof IOException);
    }
}