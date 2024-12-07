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

        events = filter.getInterval() != null ? filterByInterval(events, filter.getInterval().getDays()) :
                filterByDates(events, filter.getFrom(), filter.getTo());

        return events
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    protected Stream<AnalyticsEvent> filterByInterval(Stream<AnalyticsEvent> events, int interval) {
        LocalDateTime from = LocalDateTime.now().minusDays(interval);
        return events
                .filter(e -> e.getReceivedAt() != null && from.isBefore(e.getReceivedAt()));
    }

    protected Stream<AnalyticsEvent> filterByDates(Stream<AnalyticsEvent> events, LocalDateTime from, LocalDateTime to) {
        return events
                .filter(e -> checkDate(e.getReceivedAt(), from, to));
    }

    protected boolean checkDate(LocalDateTime date, LocalDateTime from, LocalDateTime to) {
        if (date == null) {
            return false;
        }
        if (from != null && from.isAfter(date)) {
            return false;
        }
        if (to != null && to.isBefore(date)) {
            return false;
        }
        return true;
    }

}
