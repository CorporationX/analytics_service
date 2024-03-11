package faang.school.analytics.service;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper;


    @Test
    void successSavedEvent() {
        LocalDateTime timeCreated = LocalDateTime.now();
        PremiumBoughtEvent eventDto = PremiumBoughtEvent.builder()
                .receiverId(1L)
                .amountPayment(10)
                .daysSubscription(30)
                .receivedAt(timeCreated)
                .build();

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(eventDto);
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void getAnalytics() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .receiverId(1L)
                .build();
        List<AnalyticsEvent> analyticsEventStream = List.of(analyticsEvent);
        long id = analyticsEvent.getId();
        EventType eventType = analyticsEvent.getEventType();
        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType)).thenReturn(analyticsEventStream.stream());
        List<AnalyticsEvent> actualAnalyticsEvents = analyticsEventService.getAnalytics(id, eventType);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(id, eventType);
        assertEquals(analyticsEventStream, actualAnalyticsEvents);
    }

}