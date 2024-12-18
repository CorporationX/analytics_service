package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.AdBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdBoughtEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<EventHandler<AdBoughtEvent>> eventHandlers;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AdBoughtEventListener listener;

    @Test
    public void testListenEvent() throws IOException {
        byte[] messageBody = getInvalidJsonString().getBytes();
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody);

        AdBoughtEvent expectedEvent = createAdBoughtEvent(BigDecimal.valueOf(100));

        when(objectMapper.readValue(messageBody, AdBoughtEvent.class)).thenReturn(expectedEvent);

        AdBoughtEvent actualEvent = listener.listenEvent(message, AdBoughtEvent.class);

        assertEquals(expectedEvent, actualEvent);
    }



    @Test
    public void testSaveEvent() {
        AdBoughtEvent expectedEvent = createAdBoughtEvent(BigDecimal.valueOf(100));
        AnalyticsEvent expectedAnalyticsEvent = new AnalyticsEvent();
        expectedAnalyticsEvent.setEventType(EventType.AD_BOUGHT);

        when(analyticsEventMapper.dtoToEntity(expectedEvent)).thenReturn(expectedAnalyticsEvent);

        listener.handleEvent(expectedEvent);

        verify(analyticsEventService).saveEvent(expectedAnalyticsEvent);
    }

    @Test
    public void testSaveEventException() {
        AdBoughtEvent expectedEvent = createAdBoughtEvent(BigDecimal.valueOf(100));
        AnalyticsEvent expectedAnalyticsEvent = new AnalyticsEvent();
        expectedAnalyticsEvent.setEventType(EventType.AD_BOUGHT);

        when(analyticsEventMapper.dtoToEntity(expectedEvent)).thenReturn(expectedAnalyticsEvent);
        doThrow(new RuntimeException("Database error")).when(analyticsEventService).saveEvent(expectedAnalyticsEvent);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> listener.handleEvent(expectedEvent));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testListenEventEmptyMessage() {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("".getBytes());

        InvalidMessageException exception = assertThrows(InvalidMessageException.class,
                () -> listener.listenEvent(message, AdBoughtEvent.class));
        assertEquals("Message body is empty", exception.getMessage());
    }


    private AdBoughtEvent createAdBoughtEvent(BigDecimal paymentAmount) {
        return AdBoughtEvent.builder()
                .postId(1L)
                .actorId(1L)
                .paymentAmount(paymentAmount)
                .adDuration(30L)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    private static @NotNull String getInvalidJsonString() {
        return "{\"postId\":1,\"userId\":1,\"paymentAmount\":100,\"adDuration\":30,\"receivedAt\":\"2022-01-01T12:00:00\"\"";
    }
}
