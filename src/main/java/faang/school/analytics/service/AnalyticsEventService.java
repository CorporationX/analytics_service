package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventRequestDto;
import faang.school.analytics.dto.interval.IntervalDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.IntervalMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final IntervalMapper intervalMapper;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Transactional
    public AnalyticsEventDto createEvent(AnalyticsEventRequestDto eventDto, EventType eventType) {
        AnalyticsEvent event = analyticsEventMapper.toEntity(eventDto);
        event.setEventType(eventType);
        event.setReceivedAt(LocalDateTime.now());
        saveEvent(event);
        return analyticsEventMapper.toDto(event);
    }

    @Transactional
    public void deleteEvent(long eventId) {
        analyticsEventRepository.deleteById(eventId);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, IntervalDto intervalDto, EventType eventType,
                                                LocalDateTime from, LocalDateTime to) {

        Interval interval = intervalDto.start() != null ? intervalMapper.toEntity(intervalDto) : null;
        List<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType).toList();
        if (events.isEmpty()) {
            log.warn("No events found for receiverId: {} and eventType: {}", receiverId, eventType);
            throw new EntityNotFoundException(String.format("No events found for receiverId: %d and eventType: %s",
                    receiverId, eventType));
        }

        List<AnalyticsEvent> filteredEvents = filterEvents(events, interval, from, to, eventType);
        return analyticsEventMapper.toDto(filteredEvents);
    }

    private boolean isEventInInterval(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            return interval.contains(event.getReceivedAt());
        } if (from == null || to == null) {
            log.warn("Interval or both from and to must be provided while getting analytics");
            throw new DataValidationException("Interval or both from and to must be provided");
        } else {
            return event.isBetween(from, to);
        }
    }
    private List<AnalyticsEvent> filterEvents(List<AnalyticsEvent> events, Interval interval,
                                              LocalDateTime from, LocalDateTime to, EventType eventType) {
        return events.stream()
                .filter(event -> isEventInInterval(event, interval, from, to) && event.isType(eventType))
                .sorted(AnalyticsEvent::compareByReceivedAt)
                .toList();
    }
}
