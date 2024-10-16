package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostViewEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventServiceImpl;
    @Spy
    private AnalyticsEventMapper mapper;

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @InjectMocks
    private PostViewEventListener listener;

    @Test
    void onMessageSuccess() throws IOException {
        Message message = mock(Message.class);
        byte[] body = "{\"receiverId\":1,\"actorId\":2,\"receivedAt\":[2024,10,9,10,54,42,817035000]}".getBytes();
        when(message.getBody()).thenReturn(body);

        listener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(body, PostViewEvent.class);
        verify(analyticsEventServiceImpl, times(1)).saveEvent(analyticsEventCaptor.capture());
        Assertions.assertDoesNotThrow(() -> {
            listener.onMessage(message, null);
        });
    }

    @Test
    void onMessageIOException() throws IOException {
        Message message = mock(Message.class);
        byte[] body = "invalid message".getBytes();
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, PostViewEvent.class)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> {
            listener.onMessage(message, null);
        });

        verify(objectMapper, times(1)).readValue(body, PostViewEvent.class);
        verifyNoMoreInteractions(mapper, analyticsEventServiceImpl);
    }
}