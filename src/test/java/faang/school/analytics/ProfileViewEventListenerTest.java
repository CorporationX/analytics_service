package faang.school.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProfileViewEvent;
import faang.school.analytics.listener.ProfileViewCreateEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private ProfileViewCreateEventListener profileViewEventListener;
    private ProfileViewEvent event;
    private AnalyticsEvent analyticsEvent;
    private Message message;

    @BeforeEach
    void setUp() {
        event = ProfileViewEvent.builder()
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .receiverId(2L)
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .actorId(1L)
                .receivedAt(LocalDateTime.now())
                .receiverId(2L)
                .eventType(EventType.PROFILE_VIEW)
                .build();

        message = mock(Message.class);
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        String jsonEvent = "{\"receiverId\":123,\"actorId\":456,\"receivedAt\":\"2023-10-01T12:34:56\"}";
        byte[] messageBody = jsonEvent.getBytes();

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, ProfileViewEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toAnalyticsFromUserProfileView(event)).thenReturn(analyticsEvent);

        profileViewEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(messageBody, ProfileViewEvent.class);
        verify(analyticsEventMapper, times(1)).toAnalyticsFromUserProfileView(event);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);

    }

    @Test
    void testOnMessageWithInvalidJson() throws Exception {
        byte[] messageBody = "invalid-json".getBytes();
        when(message.getBody()).thenReturn(messageBody);

        when(objectMapper.readValue(messageBody, ProfileViewEvent.class))
                .thenThrow(new IOException("Invalid JSON"));

        assertThrows(RuntimeException.class, () -> profileViewEventListener.onMessage(message, null));
    }

}
