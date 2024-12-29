package faang.school.analytics.listener.fundraised;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.fundraised.FundRaisedEvent;
import faang.school.analytics.mapper.fundraised.FundRaisedMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundRaisedEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private FundRaisedMapper fundRaisedMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private FundRaisedEventListener fundRaisedEventListener;

    @Test
    void testSaveMethodIsCalled() throws IOException {
        byte[] byteArray = new byte[0];
        Message messageMock = mock(Message.class);
        AnalyticsEventDto analyticsEventDto = new AnalyticsEventDto();
        FundRaisedEvent fundRaisedEvent = new FundRaisedEvent();
        when(messageMock.getBody()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, FundRaisedEvent.class)).thenReturn(fundRaisedEvent);
        when(fundRaisedMapper.fundRaisedtoAnalyticsEventDto(fundRaisedEvent)).thenReturn(analyticsEventDto);

        fundRaisedEventListener.onMessage(messageMock, byteArray);

        verify(messageMock).getBody();
        verify(objectMapper).readValue(byteArray, FundRaisedEvent.class);
        verify(analyticsEventService).saveFundRaisedEvent(analyticsEventDto);
    }

    @Test
    void testIoExceptionGetWrappedAsRuntimeException() throws IOException {
        byte[] byteArray = new byte[0];
        Message messageMock = mock(Message.class);
        when(objectMapper.readValue(byteArray, FundRaisedEvent.class)).thenThrow(new IOException());

        Assertions.assertThrows(RuntimeException.class,
                () -> fundRaisedEventListener.onMessage(messageMock, byteArray));
    }
}