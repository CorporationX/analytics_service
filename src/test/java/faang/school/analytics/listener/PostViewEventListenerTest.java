package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.listner.PostViewEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostViewEventListenerTest {

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private AnalyticsEvent analyticsEvent;

    @Mock
    private Message message;

    @Test
    void testHandlePostViewEventThenSave() throws IOException {
        String json = "{\"key\":\"value\"}";
        PostViewEvent event = new PostViewEvent();
        byte[] body = json.getBytes();

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, PostViewEvent.class)).thenReturn(event);
        when(analyticsEventMapper.toEntity(event)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, null);

        verify(message).getBody();
        verify(objectMapper).readValue(body, PostViewEvent.class);
        verify(analyticsEventMapper).toEntity(event);
        verify(analyticsEvent).setEventType(EventType.POST_VIEW);
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testHandleThenThrowException() throws IOException {
        String json = "{\"key\":\"value\"}";
        byte[] body = json.getBytes();

        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, PostViewEvent.class)).thenThrow(new IOException("Mapping error"));

        postViewEventListener.onMessage(message, null);

        verify(message).getBody();
        verify(objectMapper).readValue(body, PostViewEvent.class);
        verify(analyticsEventMapper, never()).toEntity((LikeEvent) any());
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
