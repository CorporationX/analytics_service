package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder().id(1L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2025, 2, 9, 0, 0)).build();
    }

    @Test
    void saveEvent_ShouldSaveAndReturnDto() {
        when(analyticsEventRepository.save(event)).thenReturn(event);
        AnalyticsEventDto expectedDto = analyticsEventMapper.toAnalyticsEventDto(event);

        AnalyticsEventDto result = analyticsEventService.saveEvent(event);

        assertEquals(expectedDto, result);

        verify(analyticsEventRepository).save(event);
    }

    @Test
    void getAnalytics_ShouldFilterAndSortEvents() {
        // Given
        LocalDateTime from = LocalDateTime.of(2024, 2, 8, 12, 0);
        LocalDateTime to = LocalDateTime.of(2024, 2, 9, 12, 0);

        AnalyticsEvent analyticsEvent = new AnalyticsEvent(1, 1, 1, EventType.FOLLOWER,
                LocalDateTime.of(2024, 2, 7, 10, 0));
        List<AnalyticsEventDto> expected = Stream.of(analyticsEvent)
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();

        when(analyticsEventRepository.findByReceiverIdAndEventTypeThenFilterByDateAndSortByTimeDesc(1L,
                EventType.FOLLOWER, from, to))
                .thenReturn(Stream.of(analyticsEvent));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1L,
                EventType.FOLLOWER, null, from, to);

        assertEquals(expected, result);
        verify(analyticsEventRepository).findByReceiverIdAndEventTypeThenFilterByDateAndSortByTimeDesc(1L,
                EventType.FOLLOWER, from, to);
    }

    @Test
    void getAnalytics_ShouldApplyInterval() {
        Interval interval = mock(Interval.class);
        LocalDateTime now = LocalDateTime.of(2024, 2, 9, 12, 0);
        LocalDateTime calculatedFrom = now.minusDays(1);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);
            when(interval.apply(any(LocalDateTime.class))).thenReturn(calculatedFrom);
            when(analyticsEventRepository.findByReceiverIdAndEventTypeThenFilterByDateAndSortByTimeDesc(1L,
                    EventType.FOLLOWER, calculatedFrom, now))
                    .thenReturn(Stream.of(event));

            List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1L,
                    EventType.FOLLOWER, interval, null, null);

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(interval).apply(any(LocalDateTime.class));
            verify(analyticsEventRepository).findByReceiverIdAndEventTypeThenFilterByDateAndSortByTimeDesc(1L,
                    EventType.FOLLOWER, calculatedFrom, now);
        }
    }
}