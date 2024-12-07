package faang.school.analytics.service.events;

import faang.school.analytics.domain.dto.events.AnalyticsEventDto;
import faang.school.analytics.domain.dto.events.AnalyticsEventFilterDto;
import faang.school.analytics.mapper.events.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventFilter analyticsEventFilter;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEventDto saveEvent(AnalyticsEventDto eventDto) {
        eventDto.setReceivedAt(LocalDateTime.now());
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        event = analyticsEventRepository.save(event);
        return analyticsEventMapper.toDto(event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventFilterDto filter) {
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
