package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.redis.events.LikeEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AbstractEventListenerTest {
    @InjectMocks
    private LikeEventListener likeEventListener;
    @Mock
    private Message message;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testMapEvent() throws IOException {
        likeEventListener.mapEvent(message, LikeEvent.class);

        verify(objectMapper, times(1)).readValue(message.getBody(), LikeEvent.class);
    }
}
