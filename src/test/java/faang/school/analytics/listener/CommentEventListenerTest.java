package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private JsonObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private CommentEventListener commentEventListener;

    @Mock
    private Message message;

    @Test
    void testOnMessage() {

        byte[] pattern = new byte[0];
        LocalDateTime timeNow = LocalDateTime.now();

        CommentEventDto commentEventDto = CommentEventDto.builder()
                .postId(1L)
                .authorId(2L)
                .commentId(3L)
                .createdAt(timeNow)
                .build();

        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(2L)
                .eventType(EventType.POST_COMMENT)
                .receivedAt(timeNow)
                .build();

        when(message.getBody()).thenReturn(pattern);
        when(analyticsEventMapper.toEntity(commentEventDto)).thenReturn(analyticsEvent);

        when(objectMapper.fromJson(pattern, CommentEventDto.class)).thenReturn(commentEventDto);

        commentEventListener.onMessage(message, new byte[0]);

        verify(objectMapper).fromJson(pattern, (CommentEventDto.class));
        verify(analyticsEventMapper).toEntity(any(CommentEventDto.class));
        verify(analyticsEventService).save(analyticsEvent);
    }
}