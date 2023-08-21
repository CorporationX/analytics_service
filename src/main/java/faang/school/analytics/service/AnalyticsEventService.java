package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEvent event) {
        repository.save(event);
        log.info("Event: {}, has successful been saved into database", event.getEventType());
    }

    public List<AnalyticsEventDto> getAnalytics(long id, int type, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Get analytics task has started, id: {}, and type: {}", id, type);
        Iterable<AnalyticsEvent> analyticsIterable = repository.findByReceiverIdAndEventType(id, EventType.of(type));
        Stream<AnalyticsEvent> analyticsStream = StreamSupport.stream(analyticsIterable.spliterator(), false);
        return analyticsStream.filter(event -> isEventInDateRange(event, startDate, endDate))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private boolean isEventInDateRange(AnalyticsEvent event, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime receivedAt = event.getReceivedAt();

        if (receivedAt.isAfter(startDate) && receivedAt.isBefore(endDate)) {
            return true;
        }
        return receivedAt.isEqual(startDate) || receivedAt.isEqual(endDate);
    }
}
