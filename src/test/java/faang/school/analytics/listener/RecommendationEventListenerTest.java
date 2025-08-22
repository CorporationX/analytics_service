package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper eventMapper;

    @Mock
    private AnalyticsEventService service;

    @InjectMocks
    private RecommendationEventListener listener;

    @Test
    @DisplayName("Должно успешно обработать ивент")
    void onMessage_ValidMessage_ShouldProcessSuccessfully() throws Exception {
        String jsonMessage = "{" +
                "\"requesterId\":123," +
                "\"receiverId\":456," +
                "\"recommendationId\":789, " +
                "\"receivedAt\":\"2024-01-15T10:30:00\"}";
        Message message = mock(Message.class);

        byte[] messageBytes = jsonMessage.getBytes(StandardCharsets.UTF_8);
        when(message.getBody()).thenReturn(messageBytes);

        RecommendationEvent recommendationEvent = new RecommendationEvent(
                123L,
                456L,
                789L,
                LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        );
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(objectMapper.readValue(messageBytes, RecommendationEvent.class)).thenReturn(recommendationEvent);
        when(eventMapper.toEntity(recommendationEvent)).thenReturn(analyticsEvent);

        listener.onMessage(message, null);

        verify(objectMapper).readValue(messageBytes, RecommendationEvent.class);
        verify(eventMapper).toEntity(recommendationEvent);
        verify(service).saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("Выбрасывает исключение при ошибки в десериализации")
    void onMessage_InvalidJson_ShouldThrowException() throws Exception {
        String invalidJson = "invalid json";
        byte[] invalidBytes = invalidJson.getBytes(StandardCharsets.UTF_8);

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(invalidBytes);

        when(objectMapper.readValue(invalidBytes, RecommendationEvent.class))
                .thenThrow(new IOException("Ошибка при десериализации ивента-рекомендации"));

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, null));
        verify(service, never()).saveEvent(any());
    }

    @Test
    @DisplayName("Должен обработать пустую json строку")
    void onMessage_EmptyBody_ShouldThrowException() throws IOException {
        byte[] emptyBytes = new byte[0];
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(emptyBytes);

        JsonParseException exception = new JsonParseException("Empty input", null, null);
        when(objectMapper.readValue(emptyBytes, RecommendationEvent.class))
                .thenThrow(exception);

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, null));
        verify(service, never()).saveEvent(any());
    }

    @Test
    @DisplayName("Должен обработать null сообщение")
    void onMessage_NullBody_ShouldThrowException() throws IOException {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(null);

        doThrow(new IOException("Null контент"))
                .when(objectMapper)
                .readValue((byte[]) null, RecommendationEvent.class);

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, null));
        verify(service, never()).saveEvent(any());
    }
}