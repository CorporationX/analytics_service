package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.CommentEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        var commentEvent = TestDataFactory.createCommentEvent();
        var analyticsEventDto = TestDataFactory.createAnalyticsEventDtoWithPostCommentEventType();
        byte[] messageBody = new byte[]{1, 2, 3};

        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, CommentEvent.class))
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
        verifyNoMoreInteractions(objectMapper, commentEventMapper, analyticsEventService);
    }
}