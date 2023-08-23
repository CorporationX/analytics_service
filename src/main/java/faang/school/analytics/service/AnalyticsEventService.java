package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEvent event) {
        log.info("Saving event: {}", event);
        repository.save(event);
        log.info("Event saved successfully: {}", event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long id, EventType type) {
        log.info("Fetching analytics for receiverId: {} and eventType: {}", id, type);
        Iterable<AnalyticsEvent> analyticsIterable = repository.findByReceiverIdAndEventType(id, EventType.of(type));
        Stream<AnalyticsEvent> events = repository.findByReceiverIdAndEventType(id, type);
        List<AnalyticsEventDto> analytics = events.map(analyticsEventMapper::toDto)
                .sorted(Comparator.comparing(AnalyticsEventDto::getReceivedAt))
                .toList();
        log.info("Fetched {} analytics events for receiverId: {} and eventType: {}", analytics.size(), id, type);
        return analytics;
    }
}