package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {

        return analyticsEventMapper.analyticsEventToAnalyticsEventDto(analyticsEventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(
            long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {
        return analyticsEventMapper.analyticsEventListToAnalyticsEventDtoList(
                findByReceiverIdAndEventType(receiverId, eventType)
                        .filter(analyticsEvent -> intervalContainsReceivedAt(
                                analyticsEvent.getReceivedAt(), interval, from, to))
                        .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                        .toList());
    }

    private Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType) {
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
    }

    private boolean intervalContainsReceivedAt(
            LocalDateTime receivedAt, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            return interval.contains(receivedAt.toDateTime());
        } else {
            if (from != null && to != null) {
                return from.isAfter(receivedAt) && to.isBefore(receivedAt);
            } else {
                log.error("interval is null");
                throw new IllegalArgumentException("interval is null");
            }
        }
    }
}
