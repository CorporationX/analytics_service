package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MessageEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MentorshipRequestedEventListenerTest {

    private static final Long ACTOR_ID = 1L;
    private static final Long RECEIVER_ID = 2L;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;

    @Test
    public void whenEventHandle() throws IOException {
        Message message = mock(Message.class);
        byte[] pattern = new byte[]{};
        String body = "{\"actorId\":1,\"receiverId\":2,\"receivedAt\":[2024,6,5,10,48,42,365600948]}";
        String channel = "mentorship_requested_channel";
        MessageEvent messageEvent = MessageEvent.builder()
                .actorId(ACTOR_ID)
                .receiverId(RECEIVER_ID)
                .build();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .actorId(ACTOR_ID)
                .actorId(RECEIVER_ID)
                .build();

        when(message.getBody()).thenReturn(body.getBytes());
        when(message.getChannel()).thenReturn(channel.getBytes());
        when(objectMapper.readValue(body, MessageEvent.class)).thenReturn(messageEvent);
        when(analyticsEventMapper.toEntity(messageEvent)).thenReturn(analyticsEvent);

        mentorshipRequestedEventListener.onMessage(message, pattern);

        verify(analyticsEventService).saveEvent(analyticsEvent);
    }
}