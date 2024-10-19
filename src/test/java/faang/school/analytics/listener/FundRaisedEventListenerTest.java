package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapperImpl;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.FundRaisedEvent;
import faang.school.analytics.service.impl.analyticsevent.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundRaisedEventListenerTest {
    @Mock
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    ObjectMapper objectMapper;

    @Spy
    AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private FundRaisedEventListener fundRaisedEventListener;

    private FundRaisedEvent fundRaisedEvent;

    @BeforeEach
    void setUp() {
        fundRaisedEvent = FundRaisedEvent.builder().build();
        var json = """
                {
                    "userId": 1,
                    "projectId": 2,
                    "amount": 10000,
                    "donatedAt": "2024-10-15T13:35:15"
                }""";

        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    @DisplayName("On message should handle event successfully")
    void onMessageShouldHandleEventSuccessfully() throws IOException {
        var analyticsEvent =  AnalyticsEvent.builder()
                .eventType(EventType.FUND_RAISED)
                .build();

        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class)))
                .thenReturn(fundRaisedEvent);
        fundRaisedEventListener.onMessage(message, null);
        verify(analyticsEventMapper).toEntity(fundRaisedEvent);
        verify(analyticsEventService).saveEvent(analyticsEvent);
    }

    @Test
    @DisplayName("On message should Throw RuntimeException")
    void onMessageShouldThrowRuntimeExceptionWhenDeserializationFails() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                fundRaisedEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).saveEvent(any());
    }
}