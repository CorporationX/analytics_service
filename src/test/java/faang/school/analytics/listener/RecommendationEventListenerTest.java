package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.mapper.analyticsEvent.RecommendationEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.RecommendationEvent;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RecommendationEventMapper recommendationEventMapper;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    @Mock
    private Message message;
    byte[] pattern = new byte[]{};
    private RecommendationEvent recommendationEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        recommendationEvent = RecommendationEvent.builder()
                .recommendationId(1L)
                .authorId(2L)
                .receiverId(3L)
                .timestamp(localDateTime)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(2L)
                .receiverId(3L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(localDateTime)
                .build();
    }

    @Test
    void testOnMessageException() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(RecommendationEvent.class)))
                .thenThrow(new IOException("Failed to convert message to objectMapper."));

        DataTransformationException exception = assertThrows(DataTransformationException.class, () ->
                recommendationEventListener.onMessage(message, pattern));
        assertEquals("Failed to convert message to objectMapper.", exception.getMessage());
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        Mockito.when(objectMapper.readValue(Mockito.any(byte[].class), Mockito.eq(RecommendationEvent.class)))
                .thenReturn(recommendationEvent);
        Mockito.when(recommendationEventMapper.toEntity(Mockito.any(RecommendationEvent.class)))
                .thenReturn(analyticsEvent);
        Mockito.when(analyticsEventService.saveEvent(Mockito.any(AnalyticsEvent.class)))
                .thenReturn(new AnalyticsEventDto());

        byte[] mockMessageBody = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsBytes(recommendationEvent);
        Mockito.when(message.getBody()).thenReturn(mockMessageBody);

        recommendationEventListener.onMessage(message, pattern);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(Mockito.any(byte[].class), Mockito.eq(RecommendationEvent.class));
        Mockito.verify(recommendationEventMapper, Mockito.times(1))
                .toEntity(Mockito.any(RecommendationEvent.class));
        Mockito.verify(analyticsEventService, Mockito.times(1))
                .saveEvent(Mockito.any(AnalyticsEvent.class));
    }
}
