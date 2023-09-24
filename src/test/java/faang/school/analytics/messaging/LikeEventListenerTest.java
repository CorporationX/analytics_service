package faang.school.analytics.messaging;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import faang.school.analytics.service.redis.events.LikeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {
    @InjectMocks
    private LikeEventListener likeEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private Message message;
    @Mock
    private AnalyticsService analyticsService;

    @BeforeEach
    public void init() {
        likeEventListener = new LikeEventListener(objectMapper, analyticsEventMapper, analyticsService);
    }

    @Test
    public void testOnMessage() {
        LikeEvent likeEvent = new LikeEvent();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().id(1L).build();

        when(likeEventListener.mapEvent(message, LikeEvent.class)).thenReturn(likeEvent);
        when(analyticsEventMapper.likeEventToAnalyticsEvent(likeEvent)).thenReturn(analyticsEvent);

        likeEventListener.onMessage(message, null);

        verify(analyticsService, times(1)).create(analyticsEvent);
    }
}
