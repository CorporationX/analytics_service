package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostViewEventListenerTest {
    @InjectMocks
    private PostViewEventListener postViewEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEvent analyticsEvent;
    @Mock
    private Message message;

    private PostViewEvent event;
    private String json;
    private String invalidJson;
    private String testChannel;
    private byte[] body;
    private byte[] invalidBody;
    private byte[] channel;

    @BeforeEach
    void setUp() {
        event = new PostViewEvent();
        json = "{\"key\":\"value\"}";
        invalidJson = "invalid_json";
        testChannel = "test channel";
        body = json.getBytes();
        invalidBody = invalidJson.getBytes();
        channel = testChannel.getBytes();
    }

    @Test
    @DisplayName("Test of correct event saving.")
    public void testSaveEvent() throws IOException {
        when(message.getBody()).thenReturn(body);
        when(message.getChannel()).thenReturn(channel);
        when(objectMapper.readValue(any(String.class), eq(PostViewEvent.class))).thenReturn(event);
        when(analyticsEventMapper.toEntity(event)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, null);

        verify(message).getChannel();
        verify(message).getBody();
        verify(objectMapper).readValue(any(String.class), eq(PostViewEvent.class));
        verify(analyticsEventMapper).toEntity(event);
        verify(analyticsEvent).setEventType(EventType.POST_VIEW);
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("Test event saving with exception.")
    public void testSaveEventWithException() throws JsonProcessingException {
        when(message.getBody()).thenReturn(invalidBody);
        when(message.getChannel()).thenReturn(channel);
        when(objectMapper.readValue(any(String.class), eq(PostViewEvent.class))).thenThrow(new JsonProcessingException("Test exception") {});

        Exception exception = assertThrows(RuntimeException.class, () ->
            postViewEventListener.onMessage(message, null));

        String expectedMessage = "Test exception";
        String actualMessage = exception.getCause().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(message).getChannel();
        verify(message).getBody();
        verify(objectMapper).readValue(any(String.class), eq(PostViewEvent.class));
    }
}
