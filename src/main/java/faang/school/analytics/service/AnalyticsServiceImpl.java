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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final AnalyticsEventRepository eventRepository;

    public List<AnalyticsEvent> getAnalytics(long receiverId, String eventTypeString, Integer eventTypeInteger,
                                             String intervalString, Integer intervalInteger, LocalDateTimeInput startDate,
                                             LocalDateTimeInput endDate) {
        EventType eventType;
        if (eventTypeString != null) {
            eventType = EventType.valueOf(eventTypeString);
        } else {
            eventType = EventType.of(eventTypeInteger);
        }

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
        Stream<AnalyticsEvent> stream = eventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        switch (interval) {
            case DAY -> result = stream.filter(e -> isEventBetweenStartEndDate(e, LocalDateTime.now().minusDays(1), LocalDateTime.now())).toList();
            case WEEK -> result = stream.filter(e -> isEventBetweenStartEndDate(e, LocalDateTime.now().minusWeeks(1), LocalDateTime.now())).toList();
            case MONTH -> result = stream.filter(e -> isEventBetweenStartEndDate(e, LocalDateTime.now().minusMonths(1), LocalDateTime.now())).toList();
            case YEAR ->  result = stream.filter(e -> isEventBetweenStartEndDate(e, LocalDateTime.now().minusYears(1), LocalDateTime.now())).toList();
        }
        return result;
    }

    private boolean isEventBetweenStartEndDate(AnalyticsEvent event, LocalDateTime start, LocalDateTime end) {
        return event.getReceivedAt().isAfter(start) && event.getReceivedAt().isBefore(end);
    }

    private List<AnalyticsEvent> getAnalyticsByStartEndDate(long receiverId, EventType eventType,
                                                            LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(e -> isEventBetweenStartEndDate(e, start, end))
                .toList();
    }
}
