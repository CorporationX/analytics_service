package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEvent saveEventEntity(AnalyticsEventDto analyticsEventDto) {
        return analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
    }

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEventDto analyticsEventDto) {
        return analyticsEventMapper.toDto(saveEventEntity(analyticsEventDto));
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        Stream<AnalyticsEvent> events = getEventByType(receiverId, eventType);

        if (interval == null) {
            if (to != null && from != null) {
                return filterAndSortEvents(events, from, to);
            } else {
                return events.map(analyticsEventMapper::toDto)
                        .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                        .toList();
            }
        } else {
            return processAndSortEventsByInterval(events, interval);
        }
    }

    private List<AnalyticsEventDto> filterAndSortEvents(Stream<AnalyticsEvent> events,
                                                        LocalDateTime from,
                                                        LocalDateTime to) {
        return events.filter(event -> event.getReceivedAt().isAfter(from)
                        && event.getReceivedAt().isBefore(to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private List<AnalyticsEventDto> processAndSortEventsByInterval(Stream<AnalyticsEvent> events,
                                                                   Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        switch (interval) {
            case DAY -> {
                return filterAndSortEvents(events, now.minusDays(2L), now.minusDays(1L));
            }
            case WEEK -> {
                return filterAndSortEvents(events, now.minusWeeks(2L), now.minusWeeks(1L));
            }
            case MONTH -> {
                return filterAndSortEvents(events, now.minusMonths(2L), now.minusMonths(1L));
            }
            case YEAR -> {
                return filterAndSortEvents(events, now.minusYears(2L), now.minusYears(1L));
            }
            case ALL_TIME -> {
                return events.map(analyticsEventMapper::toDto)
                        .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                        .toList();
            }
            default -> throw new IllegalArgumentException("Unknown interval: " + interval);
        }
    }

    public Stream<AnalyticsEvent> getEventByType(long receiverId,
                                                 EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }
}
