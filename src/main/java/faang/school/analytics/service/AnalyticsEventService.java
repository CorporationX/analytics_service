package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnalyticsEventService {
    public static final String FROM_OR_TO_NULL_EXCEPTION = "Both 'from' and 'to' must be provided" +
            " when interval is null";
    public static final String EVENT_TYPE_NULL_EXCEPTION = "Event type can't be null";
    public static final String EVENT_NULL_EXCEPTION = "Event can't be null";
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        if (event == null) {
            log.info(EVENT_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_NULL_EXCEPTION);
        }

        analyticsEventRepository.save(event);
        return analyticsEventMapper.toAnalyticsEventDto(event);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        if (eventType == null) {
            log.info(EVENT_TYPE_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_TYPE_NULL_EXCEPTION);
        }

        List<AnalyticsEvent> events =
                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType).toList();

        Stream<AnalyticsEvent> eventStream = events.stream()
                .filter(event -> isEventTimeInRange(event.getReceivedAt(), from, to, interval));

        return analyticsEventMapper.toAnalyticsEventDtoList(
                eventStream.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed()).toList()
        );
    }

    private boolean isEventTimeInRange(LocalDateTime receivedAt, LocalDateTime from,
                                                  LocalDateTime to, Interval interval) {
        if (interval != null) {
            return isEventInInterval(receivedAt, interval);
        }

        if (from == null || to == null) {
            log.info(FROM_OR_TO_NULL_EXCEPTION);
            throw new IllegalArgumentException(FROM_OR_TO_NULL_EXCEPTION);
        }

        return receivedAt.isAfter(from) && receivedAt.isBefore(to);
    }

    private boolean isEventInInterval(LocalDateTime receivedAt, Interval interval) {
        LocalDateTime now = LocalDateTime.now();

        return switch (interval) {
            case LAST_DAY -> receivedAt.isAfter(now.minusDays(1));
            case LAST_HOUR -> receivedAt.isAfter(now.minusHours(1));
            case LAST_WEEK -> receivedAt.isAfter(now.minusWeeks(1));
            case LAST_MONTH -> receivedAt.isAfter(now.minusMonths(1));
        };
    }
}