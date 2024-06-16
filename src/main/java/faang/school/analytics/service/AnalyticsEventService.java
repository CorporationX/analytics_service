package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.Interval;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return events
                .filter(getAnalyticsEventPredicate(interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(mapper::toDto)
                .toList();
    }

    private Predicate<AnalyticsEvent> getAnalyticsEventPredicate(Interval interval, LocalDateTime from, LocalDateTime to) {
        return event -> {
            LocalDateTime receivedAt = event.getReceivedAt();

            if (interval != null) {
                long epochMilli = ZonedDateTime.of(receivedAt, ZoneId.systemDefault()).toInstant().toEpochMilli();
                return interval.contains(epochMilli);
            }

            return (from == null || !receivedAt.isBefore(from)) && (to == null || !receivedAt.isAfter(to));
        };
    }
}
