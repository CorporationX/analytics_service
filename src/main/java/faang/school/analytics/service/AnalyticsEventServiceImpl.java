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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved analytics event: {}", analyticsEvent);
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        log.info("Fetching analytics for receiverId: {}, eventType: {}, interval: {}, from: {}, to: {}",
                receiverId, eventType, interval, from, to);
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        List<AnalyticsEventDto> events = analyticsEventStream
                .filter(event -> matchesIntervalOrPeriod(event.getReceivedAt(), interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
        log.info("Retrieved {} analytics events for receiverId: {}", events.size(), receiverId);
        return events;
    }

    private boolean matchesIntervalOrPeriod(LocalDateTime date,
                                            Interval interval,
                                            LocalDateTime from,
                                            LocalDateTime to) {
        boolean matches = interval != null ?
                matchesInterval(date, interval) : (date.isAfter(from) && date.isBefore(to));
        log.debug("Checking date: {}, interval: {}, from: {}, to: {}, matches: {}", date, interval, from, to, matches);
        return matches;
    }

    private boolean matchesInterval(LocalDateTime date, Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        return switch (interval) {
            case HOUR -> date.isAfter(now.minusHours(1));
            case DAY -> date.isAfter(now.minusDays(1));
            case WEEK -> date.isAfter(now.minusWeeks(1));
            case MONTH -> date.isAfter(now.minusMonths(1));
            case YEAR -> date.isAfter(now.minusYears(1));
        };
    }
}
