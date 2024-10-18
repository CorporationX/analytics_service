package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.dto.AdBoughtEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdBoughtEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Spy
    private AnalyticsEventMapperImpl mapper;

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @InjectMocks
    private AdBoughtEventListener listener;


    @Test
    void onMessageSuccess() throws IOException {
        Message message = mock(Message.class);
        byte[] body = "{\"receiverId\":1,\"userId\":2,\"receivedAt\":[2024,10,9,10,54,42,817035000]}".getBytes();
        when(message.getBody()).thenReturn(body);

        listener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(body, AdBoughtEvent.class);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEventCaptor.capture());
        Assertions.assertDoesNotThrow(() -> {
            listener.onMessage(message, null);
        });
    }

    @Test
    void onMessageIOException() throws IOException {
        Message message = mock(Message.class);
        byte[] body = "invalid message".getBytes();
        when(message.getBody()).thenReturn(body);
        when(objectMapper.readValue(body, AdBoughtEvent.class)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> {
            listener.onMessage(message, null);
        });

        verify(objectMapper, times(1)).readValue(body, AdBoughtEvent.class);
        verifyNoMoreInteractions(mapper, analyticsEventService);
    }
}