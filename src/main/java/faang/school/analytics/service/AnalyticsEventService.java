package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    public AnalyticsDto getAnalytics(Long userId, EventType eventType, Interval interval,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        Stream<AnalyticsEvent> requiredEvents = null;
        LocalDateTime today = LocalDateTime.now();
        if (interval != null) {
            switch (interval) {
                case DAY -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusDays(1), today);
                case WEEK -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusDays(7), today);
                case MONTH -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusMonths(1), today);
                case YEAR -> requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                        userId, eventType, today.minusYears(1), today);
            }
        }
        if (startDate != null && endDate != null) {
            requiredEvents = analyticsEventRepository.findOrderedAndFilteredByTimeInterval(
                    userId, eventType, startDate, endDate);
        }
        return new AnalyticsDto(userId, requiredEvents.map(AnalyticsEvent::getActorId).toList(), eventType);
    }
}
