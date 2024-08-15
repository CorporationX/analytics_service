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
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Saved event: {}", event);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(Long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        Stream<AnalyticsEvent> analyticsEventStream;

        if (interval == null) {
             analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                    receiverId, eventType, from, to);
        } else {
            LocalDateTime fromDate = Interval.getFromDate(interval);
            LocalDateTime toDate = LocalDateTime.now();
             analyticsEventStream = analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                    receiverId, eventType, fromDate, toDate);

        } if (analyticsEventStream == null) {
            log.error("No events found for receiver: {}, eventType: {}", receiverId, eventType);
            return Collections.emptyList();
        }

        List<AnalyticsEventDto> result = analyticsEventStream
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toDto)
                .toList();

        log.info("Returning {} analytics event(s)", result.size());
        return result;
    }
}
