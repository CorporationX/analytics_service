package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.DateRange;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public List<AnalyticsEvent> getAnalytics(long receiverId, EventType type, Interval interval, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            DateRange range = Interval.getDateRange(interval);
            start = range.getStartDate();
            end = range.getEndDate();
        }
        return analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(receiverId, type, start, end).collect(Collectors.toList());
    }
}