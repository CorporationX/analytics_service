package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PremiumBoughtEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.PremiumPeriod;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AnalyticsEventServiceImplAdditionalTests {
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePremiumBoughtEvent_shouldSaveEvent() {
        PremiumBoughtEventDto premiumBoughtEvent = new PremiumBoughtEventDto();
        premiumBoughtEvent.setUserId(1L);
        premiumBoughtEvent.setAmount(BigDecimal.valueOf(9.99));
        premiumBoughtEvent.setSubscriptionDuration(PremiumPeriod.ONE_MONTH);
        premiumBoughtEvent.setReceivedAt(LocalDateTime.now());

        AnalyticsEvent event = new AnalyticsEvent();
        event.setReceiverId(premiumBoughtEvent.getUserId());
        event.setEventType(EventType.PREMIUM_BOUGHT);
        event.setReceivedAt(premiumBoughtEvent.getReceivedAt());

        when(mapper.fromPremiumBoughtToEntity(premiumBoughtEvent)).thenReturn(event);

        analyticsEventService.saveEvent(event);

        ArgumentCaptor<AnalyticsEvent> eventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventRepository).save(eventCaptor.capture());

        AnalyticsEvent capturedEvent = eventCaptor.getValue();
        assertEquals(1L, capturedEvent.getReceiverId());
        assertEquals(EventType.PREMIUM_BOUGHT, capturedEvent.getEventType());
        assertEquals(premiumBoughtEvent.getReceivedAt(), capturedEvent.getReceivedAt());
    }
}