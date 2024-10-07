package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        List<AnalyticsEvent> analyticsEvents = retrieveAnalytics(receiverId, eventType);
        return filterAnalyticsEvents(analyticsEvents, interval, from, to);
    }

    private List<AnalyticsEvent> retrieveAnalytics(long receiverId, EventType eventType) {
        try (Stream<AnalyticsEvent> eventStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)) {
            return eventStream.collect(Collectors.toList());
        }
    }

    private List<AnalyticsEvent> filterAnalyticsEvents(List<AnalyticsEvent> events, Interval interval, LocalDateTime from, LocalDateTime to) {
        return events.stream()
                .filter(event -> filterByIntervalOrPeriod(event, interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .collect(Collectors.toList());
    }

    private boolean filterByIntervalOrPeriod(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            return (event.getReceivedAt().isAfter(interval.getFrom()) || event.getReceivedAt().isEqual(interval.getFrom())) &&
                    (event.getReceivedAt().isBefore(interval.getTo()) || event.getReceivedAt().isEqual(interval.getTo()));
        } else {
            return (event.getReceivedAt().isAfter(from) || event.getReceivedAt().isEqual(from)) &&
                    (event.getReceivedAt().isBefore(to) || event.getReceivedAt().isEqual(to));
        }
    }
}
