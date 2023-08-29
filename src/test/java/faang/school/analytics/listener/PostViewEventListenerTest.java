package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.exception.MessageReadException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventRepository repository;

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
        verify(repository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testInvalidMessageHandling() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid_message".getBytes());
        when(objectMapper.readValue(message.getBody(), PostViewEventDto.class))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(MessageReadException.class, () -> postViewEventListener.onMessage(message, new byte[0]));
        verifyNoInteractions(repository);
        verifyNoInteractions(analyticsEventMapper);
    }
}
