package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEvent event) {
        AnalyticsEvent savedAnalyticsEvent = analyticsEventRepository.save(event);
        log.info("Event successful saved {}", savedAnalyticsEvent);
    }

    public void deleteEvent(long id) {
        analyticsEventRepository.deleteById(id);
        log.info("Event successful deleted");
    }

    public List<AnalyticsEvent> getAnalytics(long id, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(id, type).toList();
    }

    public AnalyticsEvent getById(long id) {
        String exceptionMessage = String.format("Analytics event with id = %s not found", id);
        return analyticsEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from, LocalDateTime to) {

        Predicate<AnalyticsEvent> eventFilterPredicate;
        if (interval == null) {
            eventFilterPredicate = analyticsEvent -> isInInterval(from, to, analyticsEvent.getReceivedAt());
        } else {
            eventFilterPredicate = analyticsEvent -> isInInterval(interval, analyticsEvent.getReceivedAt());
        }

        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(eventFilterPredicate)
                .sorted((event1, event2) -> event2.getReceivedAt().compareTo(event1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private boolean isInInterval(LocalDateTime from, LocalDateTime to, LocalDateTime eventReceivedAt) {
        return eventReceivedAt.isAfter(from) && eventReceivedAt.isBefore(to);
    }

    private boolean isInInterval(Interval interval, LocalDateTime eventReceivedAt) {
        LocalDateTime nowTime = LocalDateTime.now();
        return switch (interval) {
            case HOUR -> eventReceivedAt.isAfter(nowTime.minusHours(1));
            case DAY -> eventReceivedAt.isAfter(nowTime.minusDays(1));
            case WEEK -> eventReceivedAt.isAfter(nowTime.minusWeeks(1));
            case MONTH -> eventReceivedAt.isAfter(nowTime.minusMonths(1));
            case YEAR -> eventReceivedAt.isAfter(nowTime.minusYears(1));
        };
    }
}