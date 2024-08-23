package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.CommentEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @InjectMocks
    private CommentEventListener commentEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private CommentEventMapper commentEventMapper;
    @Mock
    Message message;

    @Test
    void onMessage() throws IOException {
        // given - precondition
        CommentEvent commentEvent = CommentEvent.builder()
                .commentId(12L)
                .receiverId(23L)
                .authorId(34L)
                .createdAt(LocalDateTime.MIN)
                .build();
        AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                .receiverId(23L)
                .actorId(34L)
                .eventType("post_comment")
                .receivedAt(LocalDateTime.MIN)
                .build();

        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class)))
                .thenReturn(commentEvent);
        when(commentEventMapper.toAnalyticsEventDto(commentEvent))
                .thenReturn(analyticsEventDto);

        // when - action
        commentEventListener.onMessage(message, null);

        // then - verify the output
        verify(objectMapper, times(1))
                .readValue(any(byte[].class), eq(CommentEvent.class));
        verify(commentEventMapper, times(1))
                .toAnalyticsEventDto(commentEvent);
        verify(analyticsEventService, times(1))
                .saveEvent(analyticsEventDto);
        verifyNoInteractions(objectMapper, commentEventMapper, analyticsEventService);
    }
}