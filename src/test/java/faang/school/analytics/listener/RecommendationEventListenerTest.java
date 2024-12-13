package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.AnalyticsEventDto;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapper mapper;

    @InjectMocks
    RecommendationEventListener eventListener;

    @Test
    public void onMessagePositiveTest() throws IOException {
        RecommendationEvent event = setEvent();
        AnalyticsEventDto mappedEvent = mapper.recommendationToAnalyticsDto(event);

        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), RecommendationEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);

        eventListener.onMessage(message, messageBody);

        verify(analyticsEventService).saveAction(mappedEvent);
    }

    @Test
    public void onMessageExceptionTest() throws IOException {
        RecommendationEvent event = setEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), RecommendationEvent.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(RuntimeException.class, () -> eventListener.onMessage(message, messageBody));
    }

    public RecommendationEvent setEvent() {
        return new RecommendationEvent(1L, 1L, 1L, LocalDateTime.now());
    }
}
