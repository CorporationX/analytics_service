package faang.school.analytics.service;

import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public void save(AnalyticsEventDto analyticsEventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved analytics event: {}", analyticsEvent.getEventType());
    }

    public List<AnalyticsEventDto> getAnalytics(long id, EventType type, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Get analytics task has started, id: {}, and type: {}", id, type);
        Iterable<AnalyticsEvent> analyticsIterable = (Iterable<AnalyticsEvent>) analyticsEventRepository.findByReceiverIdAndEventType(id, type);
        Stream<AnalyticsEvent> analyticsStream = StreamSupport.stream(analyticsIterable.spliterator(), false);
        return analyticsStream.filter(event -> isEventInDateRange(event, startDate, endDate))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private boolean isEventInDateRange(AnalyticsEvent event, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime receivedAt = event.getReceivedAt();

        return !(receivedAt.isBefore(startDate) || receivedAt.isAfter(endDate));
    }
}
