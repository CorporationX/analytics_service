package faang.school.analytics.service;

import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.mapper.PremiumBoughtMapperImpl;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtServiceTest {
    @InjectMocks
    private PremiumBoughtService premiumBoughtService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private PremiumBoughtMapperImpl premiumBoughtMapper;


    @Test
    void successSavedEvent() {
        LocalDateTime timeCreated = LocalDateTime.now();
        PremiumBoughtEventDto eventDto = PremiumBoughtEventDto.builder()
                .receiverId(1L)
                .amountPayment(10)
                .daysSubscription(30)
                .receivedAt(timeCreated)
                .build();

        AnalyticsEvent eventEntity = premiumBoughtMapper.toAnalyticsEvent(eventDto);
        eventEntity.setEventType(EventType.PREMIUM_BOUGHT);
        premiumBoughtService.saveEvent(eventDto);
        verify(analyticsEventRepository, times(1)).save(eventEntity);
    }

    @Test
    void getAnalytics() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .eventType(EventType.PREMIUM_BOUGHT)
                .receiverId(1L)
                .build();
        Stream<AnalyticsEvent> analyticsEventStream = Stream.of(analyticsEvent);
        long id = analyticsEvent.getId();
        EventType eventType = analyticsEvent.getEventType();
        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType)).thenReturn(analyticsEventStream);
        Stream<AnalyticsEvent> actualAnalyticsEvents = premiumBoughtService.getAnalytics(id, eventType);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(id, eventType);
        assertEquals(analyticsEventStream, actualAnalyticsEvents);
    }
}