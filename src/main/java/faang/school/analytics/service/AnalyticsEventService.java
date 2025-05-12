package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.util.EnumConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Validated
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEventDto saveEvent(@Valid AnalyticsEventDto eventDto) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        return analyticsEventMapper.toDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, String eventType, String interval, LocalDateTime from, LocalDateTime to) {

        EventType type = EnumConverter.fromValue(EventType.class, eventType);
        Interval intervalObj = EnumConverter.fromValue(Interval.class, interval);

        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type);

        if (interval != null) {
            LocalDateTime fromDate = Interval.getFromDate(intervalObj);
            analyticsEvents = analyticsEvents.filter(event -> event.getReceivedAt().isAfter(fromDate));

        } else {
            analyticsEvents = analyticsEvents.filter(event -> event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to));
        }

        return analyticsEvents
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Predicate<AnalyticsEvent> periodFilter(LocalDateTime from, LocalDateTime to) {

        return event -> {
            LocalDateTime eventTime = event.getReceivedAt();
            return eventTime != null
                    && !eventTime.isBefore(from)
                    && !eventTime.isAfter(to);
        };
    }

}
