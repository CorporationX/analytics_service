package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper mapper;

    public void saveEvent(AnalyticsEventDto eventDto) {
        AnalyticsEvent event = mapper.toEntity(eventDto);
        repository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        return repository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> filterByIntervalOrDate(event, interval, from, to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private boolean filterByIntervalOrDate(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            return interval.isWithinInterval(event.getReceivedAt());
        }
        if (from != null && event.getReceivedAt().isBefore(from)) {
            return false;
        }
        return to == null || !event.getReceivedAt().isAfter(to);
    }
}