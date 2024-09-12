package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtEventListenerTest {
    private static final String MESSAGE_ERROR = "ReadValueException";
    private static final long VALID_ID = 1L;
    private static final long RANDOM_LONG = 1000L;
    private final String STRING = "test";
    private PremiumBoughtEvent event;
    private AnalyticsEvent analyticsEvent;
    private Message message;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventMapper mapper;
    @Mock
    private AnalyticsEventService service;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private PremiumBoughtEventListener listener;

    @BeforeEach
    void setUp() {
        LocalDateTime time = LocalDateTime.now();
        message = new Message() {
            @Override
            public byte[] getBody() {
                return STRING.getBytes();
            }

            @Override
            public byte[] getChannel() {
                return new byte[0];
            }
        };
        event = PremiumBoughtEvent.builder()
                .userId(VALID_ID)
                .paymentAmount(RANDOM_LONG)
                .subscriptionDuration(RANDOM_LONG)
                .timestamp(time)
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .eventType(EventType.PREMIUM_BOUGHT)
                .actorId(VALID_ID)
                .receivedAt(time)
                .build();
    }

    @Test
    void testOnMessageValid() throws IOException {
        //Act
        Mockito.when(objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class)).thenReturn(event);
        Mockito.when(mapper.toAnalyticsEvent(Mockito.any())).thenReturn(analyticsEvent);
        listener.onMessage(message, new byte[]{});
        //Assert
        Mockito.verify(service).saveEvent(Mockito.any());
    }

    @Test
    void testOnMessageInvalid() throws IOException {
        //Act
        Mockito.when(objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class))
                .thenThrow(new IOException());
        Mockito.when(messageSource.getMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(MESSAGE_ERROR);
        //Assert
        assertEquals(MESSAGE_ERROR,
                assertThrows(RuntimeException.class,
                        () -> listener.onMessage(message, new byte[]{})).getMessage());
    }
}