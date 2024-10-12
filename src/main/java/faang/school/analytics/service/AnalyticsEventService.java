package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final IntervalService intervalService;

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
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }

    private List<AnalyticsEvent> filterAnalyticsEvents(List<AnalyticsEvent> events, Interval interval, LocalDateTime from, LocalDateTime to) {
        return events.stream()
                .filter(event -> filterByIntervalOrPeriod(event, interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
    }

    private boolean filterByIntervalOrPeriod(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        LocalDateTime eventDate = event.getReceivedAt();

        if (interval != null) {
            LocalDateTime calculatedFrom = intervalService.getFrom(interval);
            LocalDateTime calculatedTo = intervalService.getTo();
            return DateUtils.isBetweenInclusive(eventDate, calculatedFrom, calculatedTo);
        } else {
            return DateUtils.isBetweenInclusive(eventDate, from, to);
        }
    }
}