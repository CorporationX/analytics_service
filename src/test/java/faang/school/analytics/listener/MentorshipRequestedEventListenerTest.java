package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private MentorshipRequestedEventListener listener;

    private byte[] messageBody;

    @BeforeEach
    void setUp(){
        messageBody = "{\"receiverId\":1,\"actorId\":2,\"receivedAt\":null}".getBytes();
    }

    @Test
    void onMessageSuccessfully() throws Exception {
        MentorshipRequestEvent event = new MentorshipRequestEvent(1L, 2L, null);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, MentorshipRequestEvent.class))
                .thenReturn(event);

        listener.onMessage(message, null);

        verify(objectMapper, times(1))
                .readValue(messageBody, MentorshipRequestEvent.class);
        verify(analyticsEventService, times(1))
                .saveAnalyticsEvent(event);
    }

    @Test
    void onMessageIOException() throws Exception {
        byte[] messageBody = "invalid json".getBytes();

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, MentorshipRequestEvent.class))
                .thenThrow(new IOException("Deserialization error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> listener.onMessage(message, null));
        assertEquals("IO error occurred while processing mentorship request event.",
                thrown.getMessage());

        verify(objectMapper, times(1))
                .readValue(messageBody, MentorshipRequestEvent.class);
        verifyNoInteractions(analyticsEventService);
    }

    @Test
    void onMessageUnexpectedException() throws Exception {
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, MentorshipRequestEvent.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> listener.onMessage(message, null));
        assertEquals("Unexpected error while processing mentorship request event.",
                thrown.getMessage());

        verify(objectMapper, times(1))
                .readValue(messageBody, MentorshipRequestEvent.class);
        verifyNoInteractions(analyticsEventService);
    }
}