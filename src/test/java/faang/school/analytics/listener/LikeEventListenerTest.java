package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeEventListenerTest {

    @InjectMocks
    private LikeEventListener likeEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Test
    public void testOnMessage() throws IOException {

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        LikeEvent likeEvent = LikeEvent.builder().postId(1L).build();
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn("{\"id\":\"4\"}".getBytes());
        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEvent);
        when(analyticsEventMapper.toEntity(likeEvent)).thenReturn(analyticsEvent);//

        likeEventListener.onMessage(message, null);

        verify(analyticsEventMapper, times(1)).toEntity(likeEvent);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}
