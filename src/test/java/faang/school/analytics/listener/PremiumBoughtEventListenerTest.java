package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PremiumBoughtEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.*;

class PremiumBoughtEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @InjectMocks
    private PremiumBoughtEventListener premiumBoughtEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessage_Success() throws Exception {
        String messageBody = "{\"userId\":\"123\",\"premiumType\":\"gold\"}";
        byte[] pattern = new byte[0];
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        PremiumBoughtEventDto expectedEvent = new PremiumBoughtEventDto();
        expectedEvent.setUserId(123L);
        expectedEvent.setPremiumType("gold");

        when(objectMapper.readValue(message.getBody(), PremiumBoughtEventDto.class)).thenReturn(expectedEvent);

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        when(analyticsEventMapper.fromPremiumBoughtToEntity(expectedEvent)).thenReturn(analyticsEvent);

        premiumBoughtEventListener.onMessage(message, pattern);

        verify(analyticsEventMapper).fromPremiumBoughtToEntity(expectedEvent);
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    void testOnMessage_Failure() throws Exception {
        byte[] pattern = new byte[0];
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("invalid message".getBytes());

        when(objectMapper.readValue(message.getBody(), PremiumBoughtEventDto.class))
                .thenThrow(new RuntimeException("Parsing error"));

        premiumBoughtEventListener.onMessage(message, pattern);

       verify(analyticsEventMapper, never()).fromPremiumBoughtToEntity(any());
        verify(analyticsEventService, never()).saveEvent(any());
    }
}