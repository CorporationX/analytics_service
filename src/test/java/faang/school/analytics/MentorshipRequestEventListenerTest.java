package faang.school.analytics;

import faang.school.analytics.listener.MentorshipRequestedEventListener;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private MentorshipRequestedEventListener eventListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventListener = new MentorshipRequestedEventListener(objectMapper, analyticsMapper, analyticsEventService);
    }

    @Test
    public void testOnMessage() throws Exception {
        MentorshipRequestEvent testEvent = new MentorshipRequestEvent(11, 22, LocalDateTime.now());
        String testEventJson = "{\n" +
                "  \"followerId\": \"123\",\n" +
                "  \"followeeId\": \"456\",\n" +
                "  \"timestamp\": \"2023-09-05T10:30:00\"\n" +
                "}";

        when(objectMapper.readValue(any(byte[].class), eq(MentorshipRequestEvent.class))).thenReturn(testEvent);
        when(analyticsMapper.toEntity(testEvent)).thenReturn(new AnalyticsEvent());

        Message testMessage = new Message() {
            @Override
            public byte[] getBody() {
                return testEventJson.getBytes();
            }

            @Override
            public byte[] getChannel() {
                return "testChannel".getBytes();
            }
        };

        eventListener.onMessage(testMessage, null);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(MentorshipRequestEvent.class));
        verify(analyticsMapper, times(1)).toEntity(testEvent);

        verify(analyticsEventService, times(1)).create(any(AnalyticsEvent.class));
    }
}