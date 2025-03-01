package faang.school.analytics.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.RecommendationEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.Assert;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {
    @InjectMocks
    private RecommendationEventListener recommendationEventListener;

    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RecommendationEventMapper recommendationEventMapper;
    @Mock
    private Message message;

    private RecommendationEventDto recommendationEventDto;
    private AnalyticsEventDto analyticsEventDto;
    byte[] pattern = new byte[]{};

    @BeforeEach
    void setUp() {
        recommendationEventDto = new RecommendationEventDto();
        recommendationEventDto.setReceiverId(1L);
        recommendationEventDto.setReceiverId(1L);
        recommendationEventDto.setCreatedAt(LocalDateTime.now());

        analyticsEventDto = new AnalyticsEventDto();
        analyticsEventDto.setReceiverId(recommendationEventDto.getReceiverId());
        analyticsEventDto.setActorId(recommendationEventDto.getRequesterId());
        analyticsEventDto.setReceivedAt(recommendationEventDto.getCreatedAt());
        analyticsEventDto.setEventType(EventType.RECOMMENDATION_RECEIVED);
    }

    @Test
    void testMessageOnFailed() throws IOException {
        when(objectMapper.readValue(message.getBody(), RecommendationEventDto.class))
                .thenThrow(new IOException("Error while processing message"));
        Assert.assertThrows(
                RuntimeException.class,
                () -> recommendationEventListener.onMessage(message, pattern));
    }

    @Test
    void testMessageOnSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), RecommendationEventDto.class))
                .thenReturn(recommendationEventDto);
        when(recommendationEventMapper.toAnalyticsEvent(recommendationEventDto)).thenReturn(analyticsEventDto);
        recommendationEventListener.onMessage(message, pattern);
        verify(analyticsEventService, Mockito.times(1)).saveEvent(analyticsEventDto);
    }
}
