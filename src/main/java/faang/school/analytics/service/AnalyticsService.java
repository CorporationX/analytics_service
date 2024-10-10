package faang.school.analytics.service;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
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
            interval = Interval.valueOf(eventTypeString);
        } else if (intervalInteger != null) {
            interval = Interval.of(eventTypeInteger);
        } else {
            return getAnalyticsByStartEndDate(receiverId, eventType, startDate.getDateTime(), endDate.getDateTime());
        }
        return getAnalyticsByInterval(receiverId, eventType, interval);
    }

    private List<AnalyticsEvent> getAnalyticsByInterval(long receiverId, EventType eventType,
                                                        Interval interval) {
        List<AnalyticsEvent> result = null;
        Stream<AnalyticsEvent> stream = eventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        switch (interval) {
            case DAY -> result = stream.filter(e -> e.getReceivedAt().isAfter(LocalDateTime.now().minusDays(1))).toList();
            case WEEK -> result = stream.filter(e -> e.getReceivedAt().isAfter(LocalDateTime.now().minusWeeks(1))).toList();
            case MONTH -> result = stream.filter(e -> e.getReceivedAt().isAfter(LocalDateTime.now().minusMonths(1))).toList();
            case YEAR ->  result = stream.filter(e -> e.getReceivedAt().isAfter(LocalDateTime.now().minusYears(1))).toList();
        }
        return result;
    }

    private List<AnalyticsEvent> getAnalyticsByStartEndDate(long receiverId, EventType eventType,
                                                            LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(e ->
                        e.getReceivedAt().isAfter(start) && e.getReceivedAt().isBefore(end))
                .toList();
    }
}
