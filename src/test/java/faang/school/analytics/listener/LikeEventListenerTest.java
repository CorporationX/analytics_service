package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.until.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Test
    void onMessage_shouldHandleLikeEvent_successfully() {
        LikeEvent event = LikeEvent.builder()
                .postId(1L)
                .authorId(2L)
                .userId(3L)
                .likedAt(LocalDateTime.parse("2024-04-01T12:00:00"))
                .type(EventType.LIKED_POST)
                .build();

        likeEventListener.onMessage(event);

        verify(analyticsEventService, times(1)).handleLikeEvent(event);
    }
}