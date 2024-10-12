package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.dto.RecommendationEventDto;
import faang.school.analytics.model.enums.EventType;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    @Test
    void testOnMessage_Success() throws Exception {

        long receiverId = 2L;
        long authorId = 1L;
        LocalDateTime timestamp = LocalDateTime.now();
        RecommendationEventDto recommendationEventDto = new RecommendationEventDto(null, authorId, receiverId, timestamp);

        String messageBody = "{\"receiverId\":" + receiverId + ", \"authorId\":" + authorId + ", \"timestamp\":\"" + timestamp + "\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        when(objectMapper.readValue(message.getBody(), RecommendationEventDto.class)).thenReturn(recommendationEventDto);

        recommendationEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> eventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(eventCaptor.capture());

        AnalyticsEvent savedEvent = eventCaptor.getValue();
        assertEquals(receiverId, savedEvent.getReceiverId());
        assertEquals(authorId, savedEvent.getActorId());
        assertEquals(EventType.RECOMMENDATION_RECEIVED, savedEvent.getEventType());
        assertEquals(timestamp, savedEvent.getReceivedAt());
    }

    @Test
    void onMessage_throwsException() throws Exception {

        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEventDto.class)))
                .thenThrow(new IOException("Error while reading"));
        assertThrows(RuntimeException.class, () -> recommendationEventListener.onMessage(message, null));
    }
}