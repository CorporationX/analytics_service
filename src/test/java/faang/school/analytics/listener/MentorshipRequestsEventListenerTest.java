package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.publishable.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestsEventListenerTest {
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @InjectMocks
    private MentorshipRequestsEventListener mentorshipRequestsEventListener;

    private long requesterId = 1L;
    private long receiverId = 2L;
    private LocalDateTime timestamp = LocalDateTime.now();
    private MentorshipRequestedEvent mentorshipRequestedEvent = MentorshipRequestedEvent.builder()
            .requesterId(requesterId)
            .receiverId(receiverId)
            .timestamp(timestamp)
            .build();
    private AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
            .actorId(requesterId)
            .receiverId(receiverId)
            .eventType(EventType.MENTORSHIP_REQUEST)
            .receivedAt(timestamp)
            .build();
    private byte[] pattern= new byte[]{};


    @Test
    public void onMessageTest() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEvent.class))
                .thenReturn(mentorshipRequestedEvent);

        mentorshipRequestsEventListener.onMessage(message, pattern);

        Mockito.verify(analyticsEventService).saveEvent(analyticsEvent);
    }
}
