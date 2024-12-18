package faang.school.analytics.listenerTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.listener.PremiumBoughtEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PremiumBoughtEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PremiumBoughtEventListener listener;


    @SneakyThrows
    @Test
    void onMessage_Positive() throws Exception {
        PremiumBoughtEvent event = new PremiumBoughtEvent(
                123L, 29.99, 7, LocalDateTime.of(2024, 12, 12, 10, 0, 0)
        );
        String json = "{\"userId\":123,\"amount\":29.99,\"duration\":7,\"endDate\":\"2024-12-12T10:00:00\"}";

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(bytes);
        when(objectMapper.readValue(bytes, PremiumBoughtEvent.class)).thenReturn(event);

        listener.onMessage(message, null);

        verify(objectMapper).readValue(bytes, PremiumBoughtEvent.class);
        verify(analyticsEventService, times(1)).processPremiumBoughtEvent(event);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEventDto.class));
    }

    @SneakyThrows
    @Test
    void onMessage_Negative_DeserializationError() throws Exception {
        String invalidJson = "INVALID_JSON";
        byte[] bytes = invalidJson.getBytes(StandardCharsets.UTF_8);

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(bytes);

        doThrow(new IOException("Deserialization error"))
                .when(objectMapper).readValue(bytes, PremiumBoughtEvent.class);

        listener.onMessage(message, null);

        verifyNoInteractions(analyticsEventService);
    }
}
