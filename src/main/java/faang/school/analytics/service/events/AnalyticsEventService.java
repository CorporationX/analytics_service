package faang.school.analytics.service.events;

import faang.school.analytics.domain.dto.events.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.AnalyticsEventFilterDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventFilter analyticsEventFilter;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEventDto saveEvent(AnalyticsEventDto eventDto) {
        if (eventDto.getId() != null) {
            throw new DataValidationException("The event must not have id for save");
        }

        eventDto.setReceivedAt(LocalDateTime.now());
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        event = analyticsEventRepository.save(event);
        return analyticsEventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filter) {
        if (filter.getInterval() != null && (filter.getFrom() != null || filter.getTo() != null)) {
            log.warn("Incorrect filter for get events: interval = {}, fromAt = {}, toAt = {}", filter.getInterval(), filter.getFrom(), filter.getTo());
            throw new DataValidationException("Search filter required 'Interval' or 'Dates'");
        }

        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(filter.getReceiverId(), filter.getEventType());
        events = filterEvents(events, filter);
        return events
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private Stream<AnalyticsEvent> filterEvents(Stream<AnalyticsEvent> events, AnalyticsEventFilterDto filter) {
       if (filter.getInterval() == null && filter.getFrom() == null && filter.getTo() == null) {
           return events;
       }
       return filter.getInterval() != null ? analyticsEventFilter.filterByInterval(events, filter.getInterval().getDays()) :
               analyticsEventFilter.filterByDates(events, filter.getFrom(), filter.getTo());
    }

}
