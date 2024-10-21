package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private CommentEventListener commentEventListener;

    @BeforeEach
    void setUp() {
        commentEventListener = new CommentEventListener(
                objectMapper,
                analyticsEventService,
                analyticsEventMapper,
                "testChannel"
        );
    }

    @Test
    void testSaveEvent() {
        CommentEvent commentEvent = new CommentEvent(1L, 2L, 3L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.toAnalyticsEvent(commentEvent)).thenReturn(analyticsEvent);

        commentEventListener.saveEvent(commentEvent);

        verify(analyticsEventMapper, times(1)).toAnalyticsEvent(commentEvent);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}