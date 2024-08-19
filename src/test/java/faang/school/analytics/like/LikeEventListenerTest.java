package faang.school.analytics.like;

import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventListenerTest {


    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private Message message;

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Test
    void onMessageTest(){
        String messageContent = "Test message";
        when(message.toString()).thenReturn(messageContent);

        LikeEventListener.messageList.clear();

        likeEventListener.onMessage(message, null);

        assertEquals(1, LikeEventListener.messageList.size());
        assertEquals(messageContent, LikeEventListener.messageList.get(0));

        verify(analyticsEventService, times(1)).saveLikeEvent(message);
    }
}
