package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.EventSavingFailureException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventServiceImpl implements AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        isExistingEvent(event);
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        log.debug("Event (id={}) saved", savedEvent.getId());
        return savedEvent;
    }

    @Override
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                LocalDateTime from, LocalDateTime to) {
        log.debug("Fetching analytics for receiverId={}, eventType={}, interval={}, from={}, to={} in progress...",
                receiverId, eventType, interval, from, to);

        List<AnalyticsEventDto> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)
                .filter(event -> isWithinDateRange(event, interval, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();

        log.debug("Fetched {} events from repository for receiverId={}, eventType={}",
                events.size(), receiverId, eventType);

        return events;
    }

    private boolean isWithinDateRange(AnalyticsEvent event, Interval interval,
                                      LocalDateTime from, LocalDateTime to) {
        LocalDateTime dateReceived = event.getReceivedAt();
        if (interval != null) {
            boolean intervalCheckResult = LocalDateTime.now()
                    .minus(interval.getDays(), interval.getUnit())
                    .isBefore(dateReceived);
            log.trace("Interval-based check: interval={}, receivedAt={}, withinRange={}", interval, dateReceived, intervalCheckResult);
            return intervalCheckResult;
        } else {
            boolean explicitCheckResult = (dateReceived.isAfter(from) && dateReceived.isBefore(to)
                    && !dateReceived.isAfter(LocalDateTime.now()));
            log.trace("Explicit range check: from={}, to={}, receivedAt={}, withinRange={}", from, to, dateReceived, explicitCheckResult);
            return explicitCheckResult;
        }
    }

    private void isExistingEvent(AnalyticsEvent event) {
        String message = String.format(
                "Save operation not permitted: event(id=%d) already exists", event.getId()
        );
        if (analyticsEventRepository.existsById(event.getId())) {
            log.error(message);
            throw new EventSavingFailureException(message);
        }
    }
}
