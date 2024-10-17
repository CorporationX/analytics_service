package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.service.FollowerEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

public class FollowerEventListenerTest {

    @Mock
    private FollowerEventService followerEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private FollowerEventListener followerEventListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnMessage_success() throws Exception {
        String messageBody = "{\"followerId\": 1, \"followedUserId\": 2, \"followedProjectId\": 3," +
                " \"subscriptionTime\": \"2023-10-14T10:15:30\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        FollowerEvent expectedEvent = new FollowerEvent(1L, 2L,
                3L, LocalDateTime.parse("2023-10-14T10:15:30"));

        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenReturn(expectedEvent);

        followerEventListener.onMessage(message, null);

        verify(followerEventService).saveFollowerEvent(expectedEvent);
        verify(objectMapper).readValue(messageBody, FollowerEvent.class);
    }

    @Test
    public void testOnMessage_failure() throws Exception {
        String messageBody = "{\"followerId\": 1, \"followedUserId\": 2}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());
        when(objectMapper.readValue(messageBody, FollowerEvent.class)).thenThrow(new RuntimeException("Parsing error"));

        followerEventListener.onMessage(message, null);

        verify(followerEventService, never()).saveFollowerEvent(any());
        verify(objectMapper).readValue(messageBody, FollowerEvent.class);
    }
}