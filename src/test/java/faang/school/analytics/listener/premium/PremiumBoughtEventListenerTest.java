package faang.school.analytics.listener.premium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.mapper.premium.PremiumBoughtEventMapperImpl;
import faang.school.analytics.service.events.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtEventListenerTest {
    @Mock
    private ObjectMapper objectMapper;
    @Spy
    private PremiumBoughtEventMapperImpl analyticsEventMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @InjectMocks
    private PremiumBoughtEventListener premiumBoughtEventListener;

    @Test
    void testOnMessagePositiveTest() throws IOException {
        PremiumBoughtEvent event = new PremiumBoughtEvent();
        AnalyticsEventDto mappedEvent = analyticsEventMapper.premiumBoughtToAnalyticsDto(event);
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class)).thenReturn(event);
        when(message.getBody()).thenReturn(messageBody);
        when(analyticsEventService.saveEvent(mappedEvent)).thenReturn(mappedEvent);

        premiumBoughtEventListener.onMessage(message, messageBody);

        verify(analyticsEventService).saveEvent(mappedEvent);
    }

    @Test
    void testOnMessageExceptionTest() throws IOException {
        PremiumBoughtEvent event = new PremiumBoughtEvent();
        Message message = mock(Message.class);
        byte[] messageBody = objectMapper.writeValueAsBytes(event);
        when(objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class)).thenThrow(JsonProcessingException.class);
        when(message.getBody()).thenReturn(messageBody);

        assertThrows(IllegalStateException.class, () -> premiumBoughtEventListener.onMessage(message, messageBody));
    }
}