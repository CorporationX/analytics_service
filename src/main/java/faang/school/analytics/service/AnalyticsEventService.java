package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.util.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEventDto eventDto) {
        var event = analyticsEventMapper.toEntity(eventDto);
        var savedEvent = analyticsEventRepository.save(event);

        return analyticsEventMapper.toDto(savedEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        Predicate<AnalyticsEvent> filterPredicate = createFilterPredicate(interval, from, to);

        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(filterPredicate)
                .map(analyticsEventMapper::toDto)
                .filter(Objects::nonNull)
                .toList();
    }

    private Predicate<AnalyticsEvent> createFilterPredicate(Interval interval, LocalDateTime from, LocalDateTime to) {
        return interval != null
                ? event -> interval.contains(event.getReceivedAt())
                : event -> isWithinRange(event.getReceivedAt(), from, to);
    }

    private boolean isWithinRange(LocalDateTime dateTime, LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null){
            throw new IllegalArgumentException("Date should not be null.");
        }
        return !dateTime.isBefore(from) && !dateTime.isAfter(to);
    }
}
