package faang.school.analytics.listener.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.RecommendationEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    private RecommendationEvent recommendationEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        recommendationEvent = RecommendationEvent.builder()
                .authorId(1L)
                .recommendationId(100L)
                .recipientId(2L)
                .timestamp(LocalDateTime.of(2024, 12, 12, 12, 12))
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .eventType(EventType.fromEventClass(recommendationEvent.getClass()))
                .actorId(1L)
                .receiverId(2L)
                .receivedAt(LocalDateTime.of(2024, 12, 12, 12, 12))
                .build();

        String json = """
                {
                    "authorId": 1,
                    "recommendationId": 100,
                    "recipientId": 2,
                    "timestamp": "2024-12-12T12:12:00"
                }""";

        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    @DisplayName("Should handle RecommendationEvent successfully")
    void onMessageShouldHandleEventSuccessfully() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEvent.class)))
                .thenReturn(recommendationEvent);
        when(analyticsEventMapper.toAnalyticsEvent(recommendationEvent)).thenReturn(analyticsEvent);

        recommendationEventListener.onMessage(message, null);

        verify(analyticsEventMapper).toAnalyticsEvent(recommendationEvent);
        verify(analyticsEventService).save(analyticsEvent);
    }

    @Test
    void onMessageShouldThrowRuntimeExceptionWhenDeserializationFails() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {});

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                recommendationEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).save(any());
    }
}