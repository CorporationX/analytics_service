package faang.school.analytics.service.analytics_event;

import faang.school.analytics.config.context.UserContext;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.analytics_event.AnalyticsEventGetDto;
import faang.school.analytics.exception.IntervalsNotValidException;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeUnit;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserContext userContext;

    @Transactional
    public AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent) {
        log.debug("Saving event in DB with event type {}", analyticsEvent.getEventType());
        return analyticsEventMapper.toAnalyticsEventDto(analyticsEventRepository.save(analyticsEvent));
    }

    @Transactional
    public List<AnalyticsEventDto> getAnalytics(AnalyticsEventGetDto analyticsEventGetDto) {
        long receiverId = userContext.getUserId();
        EventType eventType = analyticsEventGetDto.getEventType();
        TimeUnit timeUnit = analyticsEventGetDto.getTimeUnit();
        Integer timeQuantity = analyticsEventGetDto.getTimeQuantity();
        LocalDateTime from = analyticsEventGetDto.getFrom();
        LocalDateTime to =
                analyticsEventGetDto.getTo() == null ? LocalDateTime.now() : analyticsEventGetDto.getTo();

        log.debug("Getting analytics from DB by id: {} and type: {}", receiverId, eventType);
        return analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> checkEventByInterval(event, eventType, timeUnit, timeQuantity, from, to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();
    }

    private boolean checkEventByInterval(AnalyticsEvent event, EventType eventType, TimeUnit timeUnit,
                                         Integer timeQuantity, LocalDateTime from, LocalDateTime to) {
        if (timeUnit != null && timeQuantity != null) {
            log.debug("Searching events {} for interval {} {}", eventType, timeQuantity, timeUnit);
            return event.getReceivedAt().isAfter(TimeUnit.getStartDate(timeQuantity, timeUnit));
        } else if (from != null) {
            log.debug("Searching events {} from {} to {}", eventType, from, to);
            return event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to);
        } else {
            log.error("TimeUnit, timeQuantity and from date are null, cannot set up filter");
            throw new IntervalsNotValidException("All date markers are null! Please check your request!");
        }
    }
}
