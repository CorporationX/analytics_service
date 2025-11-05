package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.exception.AnalyticsValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    private static final Long RECEIVER_ID = 1L;
    private static final Long ACTOR_ID = 2L;
    private static final EventType EVENT_TYPE = EventType.PROFILE_VIEW;
    private static final Interval INTERVAL = Interval.LAST_WEEK;

    private final AnalyticsEvent event1 = AnalyticsEvent.builder()
            .id(1L)
            .receiverId(RECEIVER_ID)
            .actorId(ACTOR_ID)
            .eventType(EVENT_TYPE)
            .receivedAt(LocalDateTime.now().minusDays(1))
            .build();

    private final AnalyticsEvent event2 = AnalyticsEvent.builder()
            .id(2L)
            .receiverId(RECEIVER_ID)
            .actorId(3L)
            .eventType(EVENT_TYPE)
            .receivedAt(LocalDateTime.now().minusDays(3))
            .build();

    private final AnalyticsEvent oldEvent = AnalyticsEvent.builder()
            .id(3L)
            .receiverId(RECEIVER_ID)
            .actorId(4L)
            .eventType(EVENT_TYPE)
            .receivedAt(LocalDateTime.now().minusMonths(2))
            .build();

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private final AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Test
    void getAnalytics_WithIntervalShouldReturnFilteredAndSortedEvents() {
        List<AnalyticsEvent> mockEvents = Arrays.asList(event1, event2, oldEvent);
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(mockEvents.stream());

        List<AnalyticsEventResponseDto> result = analyticsEventService
                .getAnalytics(RECEIVER_ID, EVENT_TYPE, INTERVAL, null, null);

        assertThat(result).hasSize(2);
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        assertThat(result).allMatch(dto ->
                !dto.receivedAt().isBefore(weekAgo) && !dto.receivedAt().isAfter(LocalDateTime.now())
        );

        verify(analyticsEventRepository).findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE);
        verify(analyticsEventMapper, times(2)).toDto(any(AnalyticsEvent.class));
    }

    @Test
    void getAnalytics_WithFromToDatesShouldReturnFilteredEvents() {
        LocalDateTime from = LocalDateTime.now().minusDays(4);
        LocalDateTime to = LocalDateTime.now().minusDays(2);
        List<AnalyticsEvent> mockEvents = Arrays.asList(event1, event2, oldEvent);

        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(mockEvents.stream());

        List<AnalyticsEventResponseDto> result = analyticsEventService
                .getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertThat(result).hasSize(1);
        assertThat(result).allMatch(dto ->
                !dto.receivedAt().isBefore(from) && !dto.receivedAt().isAfter(to)
        );

        verify(analyticsEventRepository).findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE);
        verify(analyticsEventMapper, times(1)).toDto(any(AnalyticsEvent.class));
    }

    @Test
    void getAnalytics_WithNoIntervalOrDatesShouldThrowException() {
        assertThatThrownBy(() ->
                analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null,
                        null)).isInstanceOf(AnalyticsValidationException.class)
                .hasMessageContaining("Specify interval or both start and end dates");

        verifyNoInteractions(analyticsEventRepository);
        verifyNoInteractions(analyticsEventMapper);
    }

    @Test
    void getAnalytics_WithNoEventsShouldReturnEmptyList() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.empty());
        List<AnalyticsEventResponseDto> result = analyticsEventService
                .getAnalytics(RECEIVER_ID, EVENT_TYPE, INTERVAL, null, null);
        assertThat(result).isEmpty();

        verify(analyticsEventRepository).findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE);
        verifyNoInteractions(analyticsEventMapper);
    }

    @Test
    void saveEvent_ShouldSaveEventToRepository() {
        AnalyticsEvent anyEvent = AnalyticsEvent.builder()
                .id(3L)
                .receiverId(50L)
                .actorId(14L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventService.saveEvent(anyEvent);

        verify(analyticsEventRepository).save(anyEvent);
        verifyNoInteractions(analyticsEventMapper);
    }
}
