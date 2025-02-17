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
        LocalDateTime calculatedFrom = from;
        LocalDateTime calculatedTo = to;

        if (interval != null) {
            calculatedFrom = interval.getStart();
            calculatedTo = LocalDateTime.now();
        }

        List<AnalyticsEvent> events = repository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                receiverId, eventType, calculatedFrom, calculatedTo
        );

        return events.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}