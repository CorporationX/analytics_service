package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        return analyticsEventMapper.toAnalyticsEventDto(savedEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventsStream = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            LocalDateTime timeNow = LocalDateTime.now();
            from = interval.apply(timeNow);
            to = timeNow;
        }

        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;
        return analyticsEventsStream.filter(analyticsEvent ->
                        finalFrom.isBefore(analyticsEvent.getReceivedAt()) &&
                                finalTo.isAfter(analyticsEvent.getReceivedAt()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
    }
}
