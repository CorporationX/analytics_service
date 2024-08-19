package faang.school.analytics.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {


    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Test
    void onMessageTest() throws IOException {
        LikeEvent likeEvent = new LikeEvent();
        Message message = mock(Message.class);
        byte[] messageBody = "{\"postId\":123,\"authorId\":456,\"userId\":789,\"receivedAt\":\"2024-08-16T12:00:00\"}".getBytes();
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, LikeEvent.class)).thenReturn(likeEvent);

        likeEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(messageBody, LikeEvent.class);
        verify(analyticsEventService, times(1)).saveLikeEvent(likeEvent);
    }

    @Test
    void onMessageShouldThrowException() throws Exception {
        Message message = mock(Message.class);
        byte[] messageBody = "{\"invalid\":\"json\"}".getBytes();
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(messageBody, LikeEvent.class)).thenThrow(new IOException("Invalid JSON"));

        assertThrows(RuntimeException.class, () -> likeEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(messageBody, LikeEvent.class);
        verify(analyticsEventService, never()).saveLikeEvent(any(LikeEvent.class));
    }
}
