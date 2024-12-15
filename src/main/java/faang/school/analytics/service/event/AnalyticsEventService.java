package faang.school.analytics.service.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.analytic_event.AnalyticEventServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final EventMapper eventMapper;
    private final AnalyticEventServiceValidator analyticEventServiceValidator;

    public EventDto addNewEvent(EventDto eventDto) {
        log.info("mapping eventDto to AnalyticEvent");
        AnalyticsEvent analyticsEvent = eventMapper.toEntity(eventDto);

        if (analyticsEvent.getReceivedAt() == null) {
            log.info("set ReceivedAt now to AnalyticEvent");
            analyticsEvent.setReceivedAt(LocalDateTime.now());
        }
        log.info("save AnalyticEvent in db");
        analyticsEventRepository.save(analyticsEvent);

        log.info("mapping  AnalyticEvent to eventDto");
        return eventMapper.toDto(analyticsEvent);
    }

    public EventDto addNewEvent(AnalyticsEvent analyticsEvent) {
        log.info("validate analyticEvent argument");
        analyticEventServiceValidator.checkEntity(analyticsEvent);

        if (analyticsEvent.getReceivedAt() == null) {
            log.info("set ReceivedAt now to AnalyticEvent");
            analyticsEvent.setReceivedAt(LocalDateTime.now());
        }

        log.info("save AnalyticEvent in db");
        analyticsEventRepository.save(analyticsEvent);

        log.info("mapping AnalyticEvent to eventDto");
        return eventMapper.toDto(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getEventsEntity(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {
        log.info("validate eventRequestDto");
        analyticEventServiceValidator.validateInterval(interval, from, to);
        analyticEventServiceValidator.checkIdAndEvent(receiverId, eventType);

        log.info("getting stream of event from db");
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            log.info("changing localDateTime from and to as interval");
            from = getTime(interval);
            to = LocalDateTime.now();
        }

        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;

        log.info("apply filters and sort by received time");
        return events.filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(finalFrom))
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(finalTo))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .collect(Collectors.toList());
    }

    private LocalDateTime getTime(Interval interval) {
        switch (interval) {
            case DAY -> {
                return LocalDateTime.now().minusDays(1);
            }
            case WEEK -> {
                return LocalDateTime.now().minusWeeks(1);
            }
            case MONTH -> {
                return LocalDateTime.now().minusMonths(1);
            }
            default -> {
                return LocalDateTime.now().minusYears(1);
            }
        }
    }
}
