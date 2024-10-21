package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
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
class PostViewEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private PostViewEventListener postViewEventListener;

    @BeforeEach
    void setUp() {
        postViewEventListener = new PostViewEventListener(
                objectMapper,
                analyticsEventService,
                analyticsEventMapper,
                "testPostViewChannel"
        );
    }

    @Test
    void testSaveEvent() {
        PostViewEventDto postViewEventDto = new PostViewEventDto(1L, 2L, 3L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.postViewEventDtoToAnalyticsEvent(postViewEventDto)).thenReturn(analyticsEvent);

        postViewEventListener.saveEvent(postViewEventDto);

        verify(analyticsEventMapper, times(1)).postViewEventDtoToAnalyticsEvent(postViewEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}