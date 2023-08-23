package faang.school.analytics.service;

import faang.school.analytics.dto.EventDto;
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

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final PostViewEventMapper eventMapper;

    public void saveEvent(AnalyticsEventDto eventDto) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toModel(eventDto);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved event type: " + analyticsEvent.getEventType());
    }

    public List<AnalyticsEventDto> getAnalytics(long id, EventType type, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Get analytics task has started, id: {}, and type: {}", id, type);
        Iterable<AnalyticsEvent> analyticsIterable = analyticsEventRepository.findByReceiverIdAndEventType(id, type);
        Stream<AnalyticsEvent> analyticsStream = StreamSupport.stream(analyticsIterable.spliterator(), false);
        return analyticsStream.filter(event -> isEventInDateRange(event, startDate, endDate))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    public void saveEvent(PostViewEventDto eventDto) {
        AnalyticsEvent analyticsEvent = eventMapper.toModel(eventDto);
        analyticsEvent.setEventType(EventType.POST_VIEW);

        analyticsEventRepository.save(analyticsEvent);
        log.info("AnalyticsEvent: {} was saved successfully in DB", analyticsEvent.getEventType());
    }
}

    private boolean isEventInDateRange(AnalyticsEvent event, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime receivedAt = event.getReceivedAt();

        return !(receivedAt.isBefore(startDate) || receivedAt.isAfter(endDate));
    }
}