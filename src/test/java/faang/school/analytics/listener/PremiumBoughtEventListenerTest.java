package faang.school.analytics.listener;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PremiumBoughtEventListenerTest {

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @InjectMocks
    private PremiumBoughtEventListener listener;

    private PremiumBoughtEvent event;
    private AnalyticsEvent analyticsEvent;
    private byte[] messageBody;
    private byte[] pattern;

    @BeforeEach
    void setUp() {
        event = PremiumBoughtEvent.builder()
                .userId(1L)
                .paymentAmount(new BigDecimal("10.00"))
                .subscriptionDurationMonths(1)
                .purchaseDateTime(LocalDateTime.now())
                .build();

        analyticsEvent = AnalyticsEvent.builder()
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .receivedAt(LocalDateTime.now())
                .build();

        messageBody = "test message body".getBytes();
        pattern = "premium.bought.*".getBytes();
    }

    @Nested
    @DisplayName("Successful processing tests")
    class SuccessfulProcessingTests {

        @Test
        @DisplayName("Should successfully process valid PremiumBoughtEvent")
        void testOnMessage_SuccessfulProcessing() throws JsonProcessingException {
            // Given
            when(message.getBody()).thenReturn(messageBody);
            when(objectMapper.readValue(messageBody, PremiumBoughtEvent.class)).thenReturn(event);
            when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);
            when(analyticsEventService.save(analyticsEvent)).thenReturn(analyticsEvent);

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, pattern));

            // Then
            verify(message, times(1)).getBody();
            verify(objectMapper).readValue(messageBody, PremiumBoughtEvent.class);
            verify(analyticsEventMapper).toAnalyticsEvent(event);
            verify(analyticsEventService).save(analyticsEvent);
        }

        @Test
        @DisplayName("Should handle null pattern without errors")
        void testOnMessage_NullPattern() throws JsonProcessingException {
            // Given
            when(message.getBody()).thenReturn(messageBody);
            when(objectMapper.readValue(messageBody, PremiumBoughtEvent.class)).thenReturn(event);
            when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, null));

            // Then
            verify(objectMapper).readValue(messageBody, PremiumBoughtEvent.class);
            verify(analyticsEventMapper).toAnalyticsEvent(event);
            verify(analyticsEventService).save(analyticsEvent);
        }
    }

    @Nested
    @DisplayName("Error handling tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle deserialization exception gracefully")
        void testOnMessage_DeserializationException() throws JsonProcessingException {
            // Given
            when(message.getBody()).thenReturn(messageBody);
            when(objectMapper.readValue(messageBody, PremiumBoughtEvent.class))
                    .thenThrow(new JsonProcessingException("Failed to deserialize") {});

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, pattern));

            // Then
            verify(objectMapper).readValue(messageBody, PremiumBoughtEvent.class);
            verify(analyticsEventMapper, never()).toAnalyticsEvent(any());
            verify(analyticsEventService, never()).save(any());
        }

        @Test
        @DisplayName("Should handle null deserialization result")
        void testOnMessage_NullDeserializationResult() throws JsonProcessingException {
            // Given
            when(message.getBody()).thenReturn(messageBody);
            when(objectMapper.readValue(messageBody, PremiumBoughtEvent.class)).thenReturn(null);

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, pattern));

            // Then
            verify(objectMapper).readValue(messageBody, PremiumBoughtEvent.class);
            verify(analyticsEventMapper, never()).toAnalyticsEvent(any());
            verify(analyticsEventService, never()).save(any());
        }

        @Test
        @DisplayName("Should handle service save exception gracefully")
        void testOnMessage_ServiceException() throws JsonProcessingException {
            // Given
            when(message.getBody()).thenReturn(messageBody);
            when(objectMapper.readValue(messageBody, PremiumBoughtEvent.class)).thenReturn(event);
            when(analyticsEventMapper.toAnalyticsEvent(event)).thenReturn(analyticsEvent);
            when(analyticsEventService.save(analyticsEvent))
                    .thenThrow(new RuntimeException("Save failed"));

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, pattern));

            // Then
            verify(objectMapper).readValue(messageBody, PremiumBoughtEvent.class);
            verify(analyticsEventMapper).toAnalyticsEvent(event);
            verify(analyticsEventService).save(analyticsEvent);
        }

        @Test
        @DisplayName("Should handle null message gracefully")
        void testOnMessage_NullMessage() {
            // When
            assertDoesNotThrow(() -> listener.onMessage(null, pattern));

            // Then
            verify(objectMapper, never()).readValue(any(), any(Class.class));
            verify(analyticsEventMapper, never()).toAnalyticsEvent(any());
            verify(analyticsEventService, never()).save(any());
        }

        @Test
        @DisplayName("Should handle message with null body gracefully")
        void testOnMessage_NullMessageBody() {
            // Given
            when(message.getBody()).thenReturn(null);

            // When
            assertDoesNotThrow(() -> listener.onMessage(message, pattern));

            // Then
            verify(message).getBody();
            verify(objectMapper, never()).readValue(any(), any(Class.class));
            verify(analyticsEventMapper, never()).toAnalyticsEvent(any());
            verify(analyticsEventService, never()).save(any());
        }
    }
}
