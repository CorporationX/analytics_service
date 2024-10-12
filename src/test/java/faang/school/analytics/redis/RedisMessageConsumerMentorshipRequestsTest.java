package faang.school.analytics.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisMessageConsumerMentorshipRequestsTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisMessageConsumerMentorshipRequests redisMessageConsumerMentorshipRequests;

    @Test
    public void testOnMessage() throws IOException {
        String json = "{\"requesterId\": 12345, \"receiverId\": 67890, \"requestTime\": \"2024-10-12T10:00:00\"}";

        Message message = mock(Message.class);
        MentorshipRequestEvent expectedEvent = new MentorshipRequestEvent();
        expectedEvent.setRequesterId(12345L);
        expectedEvent.setReceiverId(67890L);
        expectedEvent.setRequestTime(LocalDateTime.parse("2024-10-12T10:00:00"));

        byte[] nameChannel = "nameChannel".getBytes();
        byte[] messageBytes = json.getBytes();

        when(objectMapper.readValue(messageBytes, MentorshipRequestEvent.class)).thenReturn(expectedEvent);
        when(message.getBody()).thenReturn(messageBytes);
        when(message.getChannel()).thenReturn(nameChannel);

        redisMessageConsumerMentorshipRequests.onMessage(message, null);

        ArgumentCaptor<MentorshipRequestEvent> eventCaptor = ArgumentCaptor.forClass(MentorshipRequestEvent.class);
        verify(analyticsEventService).saveAnalyticEvent(eventCaptor.capture());
        assertEquals(expectedEvent, eventCaptor.getValue());
    }
}