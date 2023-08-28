package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationListenerTest {
    @InjectMocks
    private RecommendationListener recommendationListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository repository;
    @Mock
    private Message message;
    private RecommendationEventDto recommendationEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        recommendationEvent = RecommendationEventDto.builder()
                .id(1L)
                .authorId(1L)
                .recipientId(2L)
                .date(dateTime)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(1L)
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(dateTime)
                .build();


        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(message.getBody(), RecommendationEventDto.class)).thenReturn(recommendationEvent);
        when(analyticsEventMapper.toEntity(recommendationEvent)).thenReturn(analyticsEvent);
    }

    @Test
    void onMessage_shouldInvokeReadValueMethod() throws IOException {
        recommendationListener.onMessage(message, null);
        verify(objectMapper).readValue(message.getBody(), RecommendationEventDto.class);
    }

    @Test
    void onMessage_shouldInvokeToEntityMethod() {
        recommendationListener.onMessage(message, null);
        verify(analyticsEventMapper).toEntity(recommendationEvent);
    }

    @Test
    void onMessage_shouldInvokeSaveMethod() {
        recommendationListener.onMessage(message, null);
        verify(repository).save(analyticsEvent);
    }
}