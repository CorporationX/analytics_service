package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    public static final String FROM_OR_TO_NULL_EXCEPTION = "Both 'from' and 'to' must be provided" +
            " when interval is null";
    public static final String EVENT_TYPE_NULL_EXCEPTION = "Event type can't be null";
    public static final String EVENT_NULL_EXCEPTION = "Event can't be null";

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository eventRepository;
    private final AnalyticsRequestParser parser;

    @Override
    public void saveCommentEvent(CommentEvent commentEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(commentEvent);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved CommentEvent to AnalyticsEvent: id={}, userId={}, commentId={}",
                analyticsEvent.getId(), analyticsEvent.getActorId(), analyticsEvent.getCommentId());
    }

    @Override
    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent event) {
        if (event == null) {
            log.info(EVENT_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_NULL_EXCEPTION);
        }

        eventRepository.save(event);
        return analyticsEventMapper.toAnalyticsEventDto(event);
    }

    @Override
    public List<AnalyticsEvent> getParseAnalytics(Long receiverId, String eventTypeRaw, String interval,
                                                  String startDate, String endDate) {

        EventType eventType = parser.parseEventType(eventTypeRaw);
        LocalDateTime start;
        LocalDateTime end;

        ZoneId zoneId = ZoneId.systemDefault();

        if (StringUtils.hasText(interval)) {
            Interval parsedInterval = parser.parseInterval(interval);
            start = parsedInterval.getStartDate(zoneId);
            end = parsedInterval.getEndDate(zoneId);
        } else if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            start = parser.parseDate(startDate);
            end = parser.parseDate(endDate);
        } else {
            throw new InvalidRequestException(MISSING_DATE_PARAMS);
        }

        log.info("Fetching analytics for receiverId={}, eventType={}, start={}, end={}", receiverId, eventType, start,
                end);
        return eventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> !event.getReceivedAt().isBefore(start) && !event.getReceivedAt().isAfter(end))
                .toList();
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        if (eventType == null) {
            log.info(EVENT_TYPE_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_TYPE_NULL_EXCEPTION);
        }

        if (interval == null && (from == null || to == null)) {
            log.info(FROM_OR_TO_NULL_EXCEPTION);
            throw new IllegalArgumentException(FROM_OR_TO_NULL_EXCEPTION);
        }

        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime start = interval != null ? interval.getStartDate(zoneId) : from;
        LocalDateTime end = interval != null ? interval.getEndDate(zoneId) : to;

        List<AnalyticsEvent> events =
                eventRepository.findByReceiverIdAndEventType(receiverId, eventType).toList();

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
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime start = interval.getStartDate(zoneId);
        LocalDateTime end = interval.getEndDate(zoneId);
        return !receivedAt.isBefore(start) && !receivedAt.isAfter(end);
    }
}
