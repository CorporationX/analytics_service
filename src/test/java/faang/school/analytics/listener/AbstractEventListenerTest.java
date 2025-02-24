package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import java.io.IOException;
import java.util.function.Consumer;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AbstractEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @Mock
    private Consumer<Object> consumer;

    private AbstractEventListener<Object> eventListener;

    @BeforeEach
    void setUp() {
        eventListener = new AbstractEventListener<>(objectMapper) {
            @Override
            public void onMessage(Message message, byte[] pattern) {

            }

            @Override
            public EventType getEventType() {
                return EventType.POST_COMMENT;
            }
        };
    }

    @Test
    void testHandleEventSuccess() throws IOException {
        String json = "{\"postAuthorId\":1,\"commentAuthorId\":2,\"postId\":3,\"commentId\":4,\"commentedAt\":\"2023-10-01T12:00:00\"}";
        byte[] jsonBytes = json.getBytes();
        Object event = new Object();

        when(message.getBody()).thenReturn(jsonBytes);
        when(objectMapper.readValue(jsonBytes, Object.class)).thenReturn(event);

        eventListener.handleEvent(message, Object.class, consumer);

        verify(objectMapper, times(1)).readValue(jsonBytes, Object.class);
        verify(consumer, times(1)).accept(event);
    }

    @Test
    void testHandleEventIOException() throws IOException {
        String json = "invalid-json";
        byte[] jsonBytes = json.getBytes();

        when(message.getBody()).thenReturn(jsonBytes);
        doThrow(IOException.class).when(objectMapper).readValue(jsonBytes, Object.class);

        assertThrows(RuntimeException.class, () ->
                eventListener.handleEvent(message, Object.class, consumer));

        verify(objectMapper, times(1)).readValue(jsonBytes, Object.class);
        verify(consumer, never()).accept(any());
    }
}