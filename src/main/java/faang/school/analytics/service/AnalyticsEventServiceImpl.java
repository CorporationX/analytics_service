package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void saveEvent(@NonNull AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Override
    public List<AnalyticsEvent> getAnalytics(
            long receiverId,
            EventType eventType,
            Interval interval,
            LocalDateTime from,
            LocalDateTime to
    ) {
        if (interval != null) {
            return filterEventsByInterval(receiverId, eventType, interval);
        } else {
            return filterEventsByDates(receiverId, eventType, from, to);
        }
    }

    private List<AnalyticsEvent> filterEventsByInterval(long receiverId, EventType eventType, Interval interval) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event ->
                        event.getReceivedAt()
                                .isAfter(LocalDateTime.now().minusSeconds(interval.toSeconds()))
                                && event.getReceivedAt()
                                .isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
    }
    private List<AnalyticsEvent> filterEventsByDates(long receiverId,
                                               EventType eventType,
                                               LocalDateTime from,
                                               LocalDateTime to
    ) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event ->
                        event.getReceivedAt()
                                .isAfter(from)
                                && event.getReceivedAt()
                                .isBefore(to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .toList();
    }
}
