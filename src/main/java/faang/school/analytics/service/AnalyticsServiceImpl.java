package faang.school.analytics.service;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final AnalyticsEventRepository eventRepository;

    public List<AnalyticsEvent> getAnalytics(long receiverId, String eventTypeString, Integer eventTypeInteger,
                                             String intervalString, Integer intervalInteger, LocalDateTimeInput startDate,
                                             LocalDateTimeInput endDate) {
        EventType eventType = Optional.ofNullable(eventTypeString)
                .map(EventType::valueOf)
                .orElseGet(() -> EventType.of(eventTypeInteger));

        Interval interval;
        if (intervalString != null) {
            interval = Interval.valueOf(intervalString);
        } else if (intervalInteger != null) {
            interval = Interval.of(intervalInteger);
        } else {
            return getAnalyticsByStartEndDate(receiverId, eventType, startDate.getDateTime(), endDate.getDateTime());
        }
        return getAnalyticsByInterval(receiverId, eventType, interval);
    }

    private List<AnalyticsEvent> getAnalyticsByInterval(long receiverId, EventType eventType,
                                                        Interval interval) {
        List<AnalyticsEvent> result = List.of();
        LocalDateTime current = LocalDateTime.now();
        switch (interval) {
            case DAY -> result = eventRepository
                    .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, current.minusDays(1), current);
            case WEEK -> result = eventRepository
                    .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, current.minusWeeks(1), current);
            case MONTH -> result = eventRepository
                    .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, current.minusMonths(1), current);
            case YEAR -> result = eventRepository
                    .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, current.minusYears(1), current);
        }
        return result;
    }

    private List<AnalyticsEvent> getAnalyticsByStartEndDate
            (long receiverId, EventType eventType, LocalDateTime start, LocalDateTime end) {
        return eventRepository.getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween
                (receiverId, eventType, start, end);
    }
}
