package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static faang.school.analytics.constants.Constants.EVENT_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.EVENT_TYPE_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.FROM_OR_TO_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.MISSING_DATE_PARAMS;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository eventRepository;
    private final AnalyticsRequestParser parser;

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

        if (StringUtils.hasText(interval)) {
            Interval parsedInterval = parser.parseInterval(interval);
            start = parsedInterval.getStartDate();
            end = parsedInterval.getEndDate();
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

        LocalDateTime start = interval != null ? interval.getStartDate() : from;
        LocalDateTime end = interval != null ? interval.getEndDate() : to;

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
        LocalDateTime start = interval.getStartDate();
        LocalDateTime end = interval.getEndDate();
        return !receivedAt.isBefore(start) && !receivedAt.isAfter(end);
    }
}
