package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.exception.CommentEventDeserializationException;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private CommentEventListener commentEventListener;

    //Positive

    @Test
    void onMessage_shouldDeserializeAndCallService() throws Exception {
        CommentEvent commentEvent = new CommentEvent();
        byte[] messageBytes = new byte[]{};

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBytes);
        when(objectMapper.readValue(messageBytes, CommentEvent.class)).thenReturn(commentEvent);

        commentEventListener.onMessage(message, null);

        verify(analyticsEventService).saveCommentEvent(commentEvent);
    }

    //Negative

    @Test
    void onMessage_shouldThrowDeserializationException() throws Exception {
        byte[] messageBytes = "invalid_json".getBytes();
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBytes);
        when(objectMapper.readValue(any(byte[].class), eq(CommentEvent.class)))
                .thenThrow(new IOException("Bad JSON"));

        assertThrows(CommentEventDeserializationException.class, () ->
            commentEventListener.onMessage(message, null));
        verifyNoInteractions(analyticsEventService);
    }
}