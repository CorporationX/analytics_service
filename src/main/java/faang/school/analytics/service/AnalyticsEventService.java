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
import java.time.format.DateTimeFormatter;
import java.util.List;
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
                                                String eventType,
                                                Object interval,
                                                String from,
                                                String to) {
        var eventTypeEnum = EventType.valueOf(eventType);
        var start = parseDateTime(from);
        var end = parseDateTime(to);

        Predicate<AnalyticsEvent> filterPredicate = createFilterPredicate(interval, start, end);

        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventTypeEnum)
                .filter(filterPredicate)
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Predicate<AnalyticsEvent> createFilterPredicate(Object interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            var intervalEnum = Interval.fromStringOrNumber(interval);
            return event -> intervalEnum.contains(event.getReceivedAt());
        } else {
            return event -> isWithinRange(event.getReceivedAt(), from, to);
        }
    }

    private boolean isWithinRange(LocalDateTime dateTime, LocalDateTime from, LocalDateTime to) {
        return !dateTime.isBefore(from) && !dateTime.isAfter(to);
    }

    private LocalDateTime parseDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(dateTime, formatter);
    }
}
