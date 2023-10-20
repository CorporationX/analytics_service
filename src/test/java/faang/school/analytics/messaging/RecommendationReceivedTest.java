package faang.school.analytics.messaging;

import faang.school.analytics.dto.RecommendationReceivedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RecommendationReceivedTest {

    @Mock
    private JsonMapper jsonMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private Message message;
    @InjectMocks
    private RecommendationReceivedEventListener recommendationReceivedEventListener;

    @Test
    void saveEvent() {
        RecommendationReceivedEvent recommendationReceivedEvent = new RecommendationReceivedEvent();
        recommendationReceivedEvent.setReceiverId(1L);
        recommendationReceivedEvent.setId(1L);
        recommendationReceivedEvent.setAuthorId(1L);
        recommendationReceivedEvent.setCreatedAt(LocalDateTime.now());

        Mockito.when(message.getBody())
                .thenReturn("s".getBytes());
        Mockito.when(jsonMapper.toObjectFromByte(message.getBody(), RecommendationReceivedEvent.class))
                .thenReturn(Optional.of(recommendationReceivedEvent));
        recommendationReceivedEventListener.onMessage(message, "".getBytes());

        Mockito.verify(analyticsEventService, Mockito.times(1))
                .recommendationReceivedEventSave(recommendationReceivedEvent);
    }
}