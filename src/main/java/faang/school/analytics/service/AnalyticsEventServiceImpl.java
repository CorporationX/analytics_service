package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsRequestDto;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    private final Clock clock;

    @Override
    public void saveCommentEvent(CommentEvent commentEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(commentEvent);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        eventRepository.save(analyticsEvent);
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
    public List<AnalyticsEvent> getParseAnalytics(AnalyticsRequestDto requestDto) {
        if (requestDto.getEventType() == null) {
            log.info(EVENT_TYPE_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_TYPE_NULL_EXCEPTION);
        }

        Instant startInstant;
        Instant endInstant;

        if (requestDto.getInterval() != null) {
            startInstant = requestDto.getInterval().getStartDate(clock);
            endInstant = requestDto.getInterval().getEndDate(clock);
        } else if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
            startInstant = requestDto.getStartDate();
            endInstant = requestDto.getEndDate();
        } else {
            log.info(FROM_OR_TO_NULL_EXCEPTION);
            throw new IllegalArgumentException(FROM_OR_TO_NULL_EXCEPTION);
        }

        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime start = LocalDateTime.ofInstant(startInstant, zone);
        LocalDateTime end = LocalDateTime.ofInstant(endInstant, zone);


        log.info("Fetching analytics for receiverId={}, eventType={}, start={}, end={}", requestDto.getReceiverId(),
                requestDto.getEventType(), start, end);

        Stream<AnalyticsEvent> all = eventRepository.findByReceiverIdAndEventType(
                requestDto.getReceiverId(), requestDto.getEventType());

        return all.filter(e -> !e.getReceivedAt().isBefore(start) && !e.getReceivedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, Instant from,
                                                Instant to) {
        if (eventType == null) {
            log.info(EVENT_TYPE_NULL_EXCEPTION);
            throw new IllegalArgumentException(EVENT_TYPE_NULL_EXCEPTION);
        }

        if (interval == null && (from == null || to == null)) {
            log.info(FROM_OR_TO_NULL_EXCEPTION);
            throw new IllegalArgumentException(FROM_OR_TO_NULL_EXCEPTION);
        }

        Instant startInstant = interval != null
                ? interval.getStartDate(clock)
                : from;
        Instant endInstant = interval != null
                ? interval.getEndDate(clock)
                : to;

        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime start = LocalDateTime.ofInstant(startInstant, zone);
        LocalDateTime end = LocalDateTime.ofInstant(endInstant, zone);

        Stream<AnalyticsEvent> all = eventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        List<AnalyticsEventDto> dtos = analyticsEventMapper.toAnalyticsEventDtoList(all
                .filter(e -> !e.getReceivedAt().isBefore(start) && !e.getReceivedAt().isAfter(end))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .collect(Collectors.toList()));

        return dtos;
    }
}

