package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalCompletedListenerTest {

    @Mock
    private ObjectMapper mapper;
    @Mock
    private AnalyticsService<GoalCompletedEvent> analyticsService;

    @InjectMocks
    private GoalCompletedListener listener;

    private GoalCompletedEvent event;

    @BeforeEach
    void setUp() {
        event = GoalCompletedEvent.builder()
                .goalId(1L)
                .userId(1L)
                .completedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void onMessage() throws IOException {

        Message message = new DefaultMessage(new byte[]{}, new byte[]{});
        byte[] pattern = new byte[]{};

        when(mapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(event);

        listener.onMessage(message, pattern);

        InOrder inOrder = inOrder(mapper, analyticsService);
        inOrder.verify(mapper).readValue(message.getBody(), GoalCompletedEvent.class);
        inOrder.verify(analyticsService).save(event);
    }
}