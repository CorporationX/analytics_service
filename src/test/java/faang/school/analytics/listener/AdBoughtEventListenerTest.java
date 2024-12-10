package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analyticsEvent.AdBoughtEventResponseDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdBoughtEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<EventHandler<AdBoughtEventResponseDto>> eventHandlers;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AdBoughtEventListener listener;

    @Test
    public void testListenEvent() throws IOException {
        byte[] messageBody = "{\"postId\":1,\"actorId\":1,\"paymentAmount\":100,\"adDuration\":30,\"timestamp\":null}".getBytes();
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody);

        AdBoughtEventResponseDto expectedEvent = AdBoughtEventResponseDto.builder()
                .postId(1L)
                .actorId(1L)
                .paymentAmount(BigDecimal.valueOf(100))
                .adDuration(30L)
                .receivedAt(LocalDateTime.now())
                .build();

        when(objectMapper.readValue(messageBody, AdBoughtEventResponseDto.class)).thenReturn(expectedEvent);

        AdBoughtEventResponseDto actualEvent = listener.listenEvent(message, AdBoughtEventResponseDto.class);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void testSaveEvent() {
        AdBoughtEventResponseDto expectedEvent = AdBoughtEventResponseDto.builder()
                .postId(1L)
                .actorId(1L)
                .paymentAmount(BigDecimal.valueOf(100))
                .adDuration(30L)
                .receivedAt(LocalDateTime.now())
                .build();


        AnalyticsEvent expectedAnalyticsEvent = new AnalyticsEvent();
        expectedAnalyticsEvent.setEventType(EventType.AD_BOUGHT);
        when(analyticsEventMapper.dtoToEntity(expectedEvent)).thenReturn(expectedAnalyticsEvent);

        listener.saveEvent(expectedEvent);

        verify(analyticsEventService).saveEvent(expectedAnalyticsEvent);
    }
}
