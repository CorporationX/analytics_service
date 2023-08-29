package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.PostViewEventDto;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] body = "test message".getBytes();
        PostViewEventDto postViewEventDto = new PostViewEventDto();
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(objectMapper.readValue(body, PostViewEventDto.class)).thenReturn(postViewEventDto);
        when(analyticsEventMapper.toEntity(postViewEventDto)).thenReturn(analyticsEvent);

        Message mockMessage = mock(Message.class);
        when(mockMessage.getBody()).thenReturn(body);

        postViewEventListener.onMessage(mockMessage, new byte[0]);

        verify(analyticsEventMapper, times(1)).toEntity(postViewEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    public void testInvalidMessageHandling() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid_message".getBytes());
        when(objectMapper.readValue(message.getBody(), PostViewEventDto.class))
                .thenThrow(new IOException("Deserialization error"));

        postViewEventListener.onMessage(message, new byte[0]);

        verifyNoInteractions(analyticsEventService);
        verifyNoInteractions(analyticsEventMapper);
    }
}
