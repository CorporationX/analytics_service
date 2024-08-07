package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        return analyticsEventMapper.toDto(analyticsEventRepository.save(event));
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> result = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        if (interval != null) {
            from = LocalDateTime.now().minus(interval.getTemporalAmount(interval));
            to = LocalDateTime.now();
        }
        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;
        return result.filter(event -> event.getReceivedAt().isAfter(finalFrom) && event.getReceivedAt().isBefore(finalTo))
                .sorted((item1, item2) -> item2.getReceivedAt().compareTo(item1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }
}
