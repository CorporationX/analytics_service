package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.analytic.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @BeforeEach
    void setUp() {
        commentEventListener = new CommentEventListener(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Test
    void testOnMessage() throws IOException {
        CommentEvent event = setEvent();
        AnalyticsEventDto mappedEvent = analyticsEventMapper.commentToAnalyticsDto(event);

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), CommentEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        commentEventListener.onMessage(message, null);

        verify(analyticsEventService).saveAction(mappedEvent);
    }

    @Test
    public void onMessageExceptionTest() throws IOException {
        CommentEvent event = setEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), RecommendationEvent.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(RuntimeException.class, () -> commentEventListener.onMessage(message, messageBody));
    }

    public CommentEvent setEvent() {
        return new CommentEvent(1L, 1L, 1L, LocalDateTime.now());
    }

}