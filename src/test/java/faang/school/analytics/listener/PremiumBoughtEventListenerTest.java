package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapperImpl;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.PremiumBoughtEvent;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PremiumBoughtEventListenerTest {

    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private PremiumBoughtEventListener projectViewEventListener;

    private Message message;
    private PremiumBoughtEvent premiumBoughtEvent;
    String json;

    @BeforeEach
    void setUp() {
        premiumBoughtEvent = PremiumBoughtEvent.builder()
                .userId(1L)
                .observeTime(LocalDateTime.of(2024, 10, 14, 13, 56, 39, 0))
                .build();
        json = "{\"userId\":1, \"observeTime\":2016-03-16T13:56:39.492}";
        message = mock(Message.class);
        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    void onMessage_shouldHandleEventSuccessfully() throws IOException {
        when(objectMapper.readValue(json.getBytes(), PremiumBoughtEvent.class)).thenReturn(premiumBoughtEvent);

        projectViewEventListener.onMessage(message, null);
        // then
        verify(analyticsEventMapper).toEntity(premiumBoughtEvent);
        verify(analyticsEventService).saveEvent(any(AnalyticsEvent.class));
        verify(analyticsEventService).saveEvent(argThat(event -> event.getEventType() == EventType.PREMIUM_BOUGHT));
        verify(analyticsEventService).saveEvent(argThat(event -> event.getReceivedAt().equals(premiumBoughtEvent.observeTime())));
    }
}
