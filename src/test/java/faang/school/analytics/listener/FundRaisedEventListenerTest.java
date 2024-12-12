package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FundRaisedEvent;
import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.mapper.fund_raised.FundRaisedEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundRaisedEventListenerTest {
    @InjectMocks
    private FundRaisedEventListener fundRaisedEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private FundRaisedEventMapper fundRaisedEventMapper;

    @Mock
    private Message message;

    private FundRaisedEvent fundRaisedEvent;

    @BeforeEach
    void setUp() {
        fundRaisedEvent = new FundRaisedEvent(1L, 100L, 500L, LocalDateTime.now());
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] messageBody = "some message".getBytes();
        when(message.getBody()).thenReturn(messageBody);
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class))).thenReturn(fundRaisedEvent);

        EventDto eventDto = new EventDto(1L, 2L, EventType.DONATION, LocalDateTime.now());
        when(fundRaisedEventMapper.toEventDto(fundRaisedEvent)).thenReturn(eventDto);

        fundRaisedEventListener.onMessage(message, null);

        verify(analyticsEventService, times(1)).addNewEvent(eventDto);
        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(FundRaisedEvent.class));
        verify(fundRaisedEventMapper, times(1)).toEventDto(fundRaisedEvent);
    }

    @Test
    void testOnMessageWithIOException() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class))).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> fundRaisedEventListener.onMessage(message, null));
    }
}
