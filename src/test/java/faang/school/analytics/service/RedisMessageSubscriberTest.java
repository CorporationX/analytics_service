package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedisMessageSubscriberTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private RedisMessageSubscriber redisMessageSubscriber;

    @Test
    public void testOnMessageWhenValidMessage() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("valid message".getBytes());
        when(objectMapper.readValue(any(String.class), eq(SearchAppearanceEvent.class)))
                .thenReturn(new SearchAppearanceEvent(1L, 2L, LocalDateTime.now()));

        redisMessageSubscriber.onMessage(message, null);

        verify(eventPublisher, times(1)).publishEvent(any(SearchAppearanceEvent.class));
    }
}