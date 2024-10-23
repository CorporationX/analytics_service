package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.model.dto.RecommendationEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {
    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private RecommendationEventListener recommendationEventListener;
    @Mock
    private Message message;
    private RecommendationEventDto recommendationEventDto;

    @BeforeEach
    void setUp() {
        recommendationEventDto = RecommendationEventDto.builder().build();
        String timeToString = LocalDateTime.now().toString();
        String json = "{\"authorId\":1, \"receiverId\":2,\"recommendedAt\":" + timeToString + "}";
        when(message.getBody()).thenReturn(json.getBytes(StandardCharsets.UTF_8));
    }
    @Test
    void onMessage_shouldHandleEventSuccessfully() throws IOException {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        when(objectMapper.readValue(any(byte[].class), eq(RecommendationEventDto.class))).thenReturn(recommendationEventDto);
        when(analyticsEventMapper.toEntity(recommendationEventDto)).thenReturn(analyticsEvent);
        // when
        recommendationEventListener.onMessage(message, null);
        // then
        verify(analyticsEventMapper, times(1)).toEntity(recommendationEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
        verify(analyticsEventService).saveEvent(argThat(event -> event.getEventType() == EventType.RECOMMENDATION_RECEIVED));
    }
}
