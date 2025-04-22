package faang.school.analytics.service;

import faang.school.analytics.dto.AggregatedAnalyticDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsGetDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public AnalyticsEventDto saveAnalytics(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent =
                analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        return analyticsEventMapper.toDto(analyticsEvent);
    }

    @Override
    public List<AggregatedAnalyticDto> getAnalytics(AnalyticsGetDto analyticsGetDto) {
        if (analyticsGetDto.getTo().isBefore(analyticsGetDto.getFrom())) {
            throw new IllegalArgumentException("To date must be after From date");
        }
        List<AnalyticsEvent> analyticsEvents = analyticsEventRepository
                .findByReceiverIdAndEventType(analyticsGetDto.getReceiverId(), analyticsGetDto.getEventType())
                .filter(event -> event.getReceivedAt().isAfter(analyticsGetDto.getFrom()) &&
                        event.getReceivedAt().isBefore(analyticsGetDto.getTo()))
                .toList();
        return analyticsEvents.stream()
                .collect(Collectors.groupingBy(
                        event -> truncateToInterval(event.getReceivedAt(), analyticsGetDto.getInterval()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                eventList -> AggregatedAnalyticDto.builder()
                                        .intervalStart(truncateToInterval(
                                                eventList.get(0).getReceivedAt(), analyticsGetDto.getInterval()))
                                        .interval(analyticsGetDto.getInterval())
                                        .eventCount(eventList.size())
                                        .build()
                        )))
                .values().stream()
                .sorted(Comparator.comparing(AggregatedAnalyticDto::getIntervalStart))
                .collect(Collectors.toList());
    }

    private LocalDateTime truncateToInterval(LocalDateTime dateTime, Interval interval) {
        return switch (interval) {
            case MINUTE -> dateTime.truncatedTo(ChronoUnit.MINUTES);
            case HOUR -> dateTime.truncatedTo(ChronoUnit.HOURS);
            case DAY -> dateTime.truncatedTo(ChronoUnit.DAYS);
            case WEEK -> dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .truncatedTo(ChronoUnit.DAYS);
            case MONTH -> dateTime.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
            default -> throw new IllegalArgumentException("Unknown interval: " + interval);
        };
    }
}
