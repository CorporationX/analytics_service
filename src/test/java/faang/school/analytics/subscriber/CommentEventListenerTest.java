package faang.school.analytics.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.service.CommentEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class CommentEventListenerTest {
    @InjectMocks
    private CommentEventListener commentEventListener;
    @Mock
    private CommentEventServiceImpl commentEventService;
    @Mock
    private ObjectMapper objectMapper;
    private CommentEvent commentEvent;
    private Message message;

    @BeforeEach
    public void init() throws IOException {
        commentEvent = new CommentEvent();
        message = Mockito.mock(Message.class);

        Mockito.lenient().when(objectMapper.readValue(message.getBody(), CommentEvent.class))
                .thenReturn(commentEvent);
    }

    @Test
    void onMessage_WhenOk() throws IOException {
        commentEventListener.onMessage(message, null);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(message.getBody(), CommentEvent.class);
        Mockito.verify(commentEventService, Mockito.times(1))
                .save(commentEvent);
    }

    @Test
    void onMessage_WhenWrongBody() throws IOException {
        Mockito.when(objectMapper.readValue(message.getBody(), CommentEvent.class))
                .thenThrow(new IOException());
        Assertions.assertThrows(RuntimeException.class, () -> commentEventListener.onMessage(message, null));

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(message.getBody(), CommentEvent.class);
    }


}
