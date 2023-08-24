package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostViewListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private PostViewListener postViewListener;

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] body = "test message".getBytes();
        PostViewEventDto postViewEventDto = new PostViewEventDto();
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(objectMapper.readValue(body, PostViewEventDto.class)).thenReturn(postViewEventDto);
        when(analyticsEventMapper.toEntity(postViewEventDto)).thenReturn(analyticsEvent);

        Message mockMessage = mock(Message.class);
        when(mockMessage.getBody()).thenReturn(body);

        postViewListener.onMessage(mockMessage, new byte[0]);

        verify(analyticsEventMapper, times(1)).toEntity(postViewEventDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessageIOException() throws IOException {
        byte[] body = "test message".getBytes();

        when(objectMapper.readValue(body, PostViewEventDto.class)).thenThrow(new IOException());

        Message mockMessage = mock(Message.class);
        when(mockMessage.getBody()).thenReturn(body);

        Assertions.assertThrows(RuntimeException.class, () -> postViewListener.onMessage(mockMessage, new byte[0]));

        verify(analyticsEventMapper, never()).toEntity(any());
        verify(analyticsEventService, never()).saveEvent(any());
    }
}
