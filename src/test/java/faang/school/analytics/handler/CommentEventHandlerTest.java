package faang.school.analytics.handler;

import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentEventHandlerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private CommentEventHandler commentEventHandler;

    private CommentEvent commentEvent;
    private AnalyticsEventDto analyticsEventDto;

    @BeforeEach
    void setUp() {
        commentEvent = new CommentEvent(1L, 123L, 456L, 2L);
        analyticsEventDto = AnalyticsEventDto.builder()
                .receiverId(1L)
                .actorId(123L)
                .postId(456L)
                .commentId(commentEvent.getCommentId())
                .eventType(EventType.POST_COMMENT)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCanHandle() {
        assertTrue(commentEventHandler.canHandle(commentEvent));
        assertFalse(commentEventHandler.canHandle(null));
    }

    @Test
    void testHandle() {
        Mockito.when(analyticsEventMapper.fromCommentEventToDto(commentEvent)).thenReturn(analyticsEventDto);

        commentEventHandler.handle(commentEvent);

        Mockito.verify(analyticsEventMapper, Mockito.times(1)).fromCommentEventToDto(commentEvent);
        Mockito.verify(analyticsEventService, Mockito.times(1)).save(analyticsEventDto);
    }
}
