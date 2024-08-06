package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl mapper;

    @Test
    void testSaveEvent() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(123)
                .build();

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testGetAnalytics_intervalNotNull() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        Interval interval = Interval.WEEK;

        AnalyticsEvent includedEvent1 = AnalyticsEvent.builder()
                .id(11L)
                .receivedAt(LocalDateTime.now().minusDays(1))
                .build();
        AnalyticsEvent includedEvent2 = AnalyticsEvent.builder()
                .id(12L)
                .receivedAt(LocalDateTime.now())
                .build();
        AnalyticsEvent excludedEventByDate = AnalyticsEvent.builder()
                .id(13L)
                .receivedAt(LocalDateTime.now().minusDays(7))
                .build();
        Stream<AnalyticsEvent> preparedStream = Stream.of(includedEvent1, includedEvent2, excludedEventByDate);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(preparedStream);

        List<AnalyticsEventDto> result =
                analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        assertEquals(2, result.size());
        assertEquals(includedEvent2.getId(), result.get(0).getId()); // testing order
        assertEquals(includedEvent1.getId(), result.get(1).getId());
        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Test
    void testGetAnalytics_intervalNullFromAndToNotNull() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        LocalDateTime from = LocalDateTime.now().minusDays(6);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent includedEvent1 = AnalyticsEvent.builder()
                .id(11L)
                .receivedAt(LocalDateTime.now().minusDays(1))
                .build();
        AnalyticsEvent includedEvent2 = AnalyticsEvent.builder()
                .id(12L)
                .receivedAt(to)
                .build();
        AnalyticsEvent excludedEventByDate = AnalyticsEvent.builder()
                .id(13L)
                .receivedAt(LocalDateTime.now().minusDays(7))
                .build();
        Stream<AnalyticsEvent> preparedStream = Stream.of(includedEvent1, includedEvent2, excludedEventByDate);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(preparedStream);

        List<AnalyticsEventDto> result =
                analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

        assertEquals(2, result.size());
        assertEquals(includedEvent2.getId(), result.get(0).getId()); // testing order
        assertEquals(includedEvent1.getId(), result.get(1).getId());
        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(receiverId, eventType);
    }
}