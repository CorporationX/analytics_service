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

    public void saveEvent(AnalyticsEvent analyticsEvent){
        analyticsEventRepository.save(analyticsEvent);
    }

    public AnalyticsDto getAnalytics(Long userId, EventType eventType, Interval interval,
                                     LocalDateTime startDate, LocalDateTime endDate){
        Stream<AnalyticsEvent> requiredEvents = analyticsEventRepository.findByReceiverIdAndEventType(userId, eventType);
        LocalDateTime now = LocalDateTime.now();
        requiredEvents = requiredEvents.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt));
        if(interval != null){
            switch (interval){
                case DAY -> requiredEvents = requiredEvents.filter(requiredEvent -> now.minusDays(1).isBefore(requiredEvent.getReceivedAt()));
                case WEEK -> requiredEvents = requiredEvents.filter(requiredEvent -> now.minusDays(7).isBefore(requiredEvent.getReceivedAt()));
                case MONTH -> requiredEvents = requiredEvents.filter(requiredEvent -> now.minusMonths(1).isBefore(requiredEvent.getReceivedAt()));
                case YEAR -> requiredEvents = requiredEvents.filter(requiredEvent -> now.minusYears(1).isBefore(requiredEvent.getReceivedAt()));
            }
        }
        if(startDate != null || endDate != null){
            requiredEvents = requiredEvents.filter(requiredEvent -> {
                        assert startDate != null;
                        return startDate.isBefore(requiredEvent.getReceivedAt());
                    })
                    .filter(requiredEvent -> {
                        assert endDate != null;
                        return endDate.isAfter(requiredEvent.getReceivedAt());
                    });
        }
        return new AnalyticsDto(userId, requiredEvents.map(AnalyticsEvent::getActorId).toList(), eventType);
    }
}
