package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.util.Interval;
import faang.school.analytics.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Test
    void givenEventWhenSaveEventThenReturnEvent() {
        // given - precondition
        var eventDto = TestDataFactory.createAnalyticsEventDto();
        var event = TestDataFactory.createAnalyticsEvent();

        when(analyticsEventMapper.toEntity(eventDto))
                .thenReturn(event);
        when(analyticsEventRepository.save(any(AnalyticsEvent.class)))
                .thenReturn(event);
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(eventDto);

        // when - action
        var actualResult = analyticsEventService.saveEvent(eventDto);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.receiverId()).isEqualTo(event.getReceiverId());
        assertThat(actualResult.actorId()).isEqualTo(event.getActorId());
        assertThat(actualResult.eventType().toUpperCase()).isEqualTo(event.getEventType().name());
        assertThat(actualResult.receivedAt().toLocalDate())
                .isEqualTo(event.getReceivedAt().toLocalDate());

        verify(analyticsEventRepository, times(1))
                .save(any(AnalyticsEvent.class));
        verify(analyticsEventMapper, times(1))
                .toDto(any(AnalyticsEvent.class));
        verify(analyticsEventMapper, times(1))
                .toEntity(any(AnalyticsEventDto.class));
    }

    @Test
    void givenIntervalWhenGetAnalyticsThenReturnAnalyticsEvents() {
        // given - precondition
        var interval = TestDataFactory.createInterval();
        long id = TestDataFactory.ID;
        var eventType = EventType.GOAL_COMPLETED;
        var start = LocalDateTime.of(2024, 1, 1, 12, 0);
        var end = LocalDateTime.of(2024, 8, 12, 12, 0);
        var eventDto = TestDataFactory.createAnalyticsEventDto();
        var event = TestDataFactory.createAnalyticsEvent();

        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType))
                .thenReturn(Stream.of(event));
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(eventDto);

        // when - action
        var actualResult = analyticsEventService
                .getAnalytics(id, eventType, interval, start, end);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.get(0).id()).isEqualTo(eventDto.id());
    }

    @Test
    void givenNullIntervalWhenGetAnalyticsThenReturnAnalyticsEvents() {
        // given - precondition
        Interval interval = null;
        long id = TestDataFactory.ID;
        var eventType = EventType.GOAL_COMPLETED;
        var start = LocalDateTime.of(2024, 1, 1, 12, 0);
        var end = LocalDateTime.of(2024, 8, 12, 12, 0);
        var eventDto = TestDataFactory.createAnalyticsEventDto();
        var event = TestDataFactory.createAnalyticsEvent();

        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType))
                .thenReturn(Stream.of(event));
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(eventDto);

        // when - action
        var actualResult = analyticsEventService
                .getAnalytics(id, eventType, interval, start, end);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.get(0).id()).isEqualTo(eventDto.id());
    }

    @Test
    void givenStartAndEndDatesWhenNoEventsThenReturnEmptyList() {
        // given - precondition
        Interval interval = null;
        long id = TestDataFactory.ID;
        var eventType = EventType.GOAL_COMPLETED;
        var start = LocalDateTime.of(2024, 1, 1, 12, 0);
        var end = LocalDateTime.of(2024, 1, 1, 12, 1);
        var event = TestDataFactory.createAnalyticsEvent();

        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType))
                .thenReturn(Stream.of(event));

        // when - action
        var actualResult = analyticsEventService
                .getAnalytics(id, eventType, interval, start, end);

        // then - verify the output
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }
}