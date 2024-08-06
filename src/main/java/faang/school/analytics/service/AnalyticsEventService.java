package faang.school.analytics.service;

import faang.school.analytics.dto.AnalylticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    public void saveEvent(AnalyticsEvent event) {
        if (event == null) {
            throw new RuntimeException("Event can't be empty");
        }
        repository.save(event);
    }

    public List<AnalylticsEventDto> getAnalytics(long receiverId,
                                                 EventType eventType,
                                                 Interval interval,
                                                 LocalDateTime from,
                                                 LocalDateTime to) {
        Stream<AnalyticsEvent> result = repository.findByReceiverIdAndEventType(receiverId, eventType);
        return result.filter(event -> matchesIntervalOrPeriod(event.getReceivedAt(), interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(mapper::toDto)
                .toList();
    }

    private boolean matchesIntervalOrPeriod(LocalDateTime date,
                                            Interval interval,
                                            LocalDateTime from,
                                            LocalDateTime to) {
        if (interval != null) {
            return matchesInterval(date, interval);
        }
        return !date.isBefore(from) && !date.isAfter(to);
    }

    private boolean matchesInterval(LocalDateTime date, Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        switch (interval) {
            case HOURLY:
                return date.isAfter(now.minusHours(1));
            case DAILY:
                return date.isAfter(now.minusDays(1));
            case WEEKLY:
                return date.isAfter(now.minusWeeks(1));
            case MONTHLY:
                return date.isAfter(now.minusMonths(1));
            default:
                return false;
        }

    }
}
