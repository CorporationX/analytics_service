package faang.school.analytics.listenerTests;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.PostViewEvent;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostViewEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    PostViewEventListenerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessage() {
        // Arrange
        PostViewEvent event = new PostViewEvent(1L, 2L, 3L, LocalDateTime.now());
        ArgumentCaptor<AnalyticsEventDto> captor = ArgumentCaptor.forClass(AnalyticsEventDto.class);

        // Act
        postViewEventListener.onMessage(event);

        // Assert
        verify(analyticsEventService, times(1)).saveEvent(captor.capture());
        AnalyticsEventDto capturedDto = captor.getValue();

        assertEquals(event.getPostId(), capturedDto.getReceiverId());
        assertEquals(event.getViewerId(), capturedDto.getActorId());
        assertEquals(EventType.POST_VIEW, capturedDto.getEventType());
        assertEquals(event.getViewedAt(), capturedDto.getReceivedAt());
    }
}