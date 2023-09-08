package faang.school.analytics.service;

import faang.school.analytics.dto.fundRasing.FundRaisedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FundRaisedEventHandlerTest {

    @Mock
    AnalyticsEventService service;

    @Spy
    AnalyticsEventMapperImpl mapper;

    @InjectMocks
    FundRaisedEventHandler handler;

    @Test
    @DisplayName("Successfully save FundRaisedEvent")
    void save_test() {
        FundRaisedEvent event = FundRaisedEvent.builder()
                .donorId(123)
                .projectId(67890)
                .amount(new BigDecimal("100.00"))
                .timestamp(LocalDateTime.parse("2023-09-07T12:34:56"))
                .build();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(null)
                .actorId(123L)
                .receiverId(67890L)
                .eventType(EventType.FUND_RAISED)
                .receivedAt(LocalDateTime.parse("2023-09-07T12:34:56"))
                .build();

        handler.save(event);

        verify(mapper, times(1)).toAnalyticsEvent(event);
        verify(service, times(1)).save(analyticsEvent);
    }
}