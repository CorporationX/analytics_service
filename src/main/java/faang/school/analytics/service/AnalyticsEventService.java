package faang.school.analytics.service;

import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.dto.event.ResponseAnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    @Transactional
    public List<ResponseAnalyticsEventDto> getAnalytics(
            long receiverId,
            EventType eventType,
            Interval interval,
            LocalDateTime from,
            LocalDateTime to) {

        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            events = filterEventsByInterval(events, interval);
        } else {
            events = filterEventsManually(events, from, to);
        }
        return events.sorted((a1, a2) -> a2.getReceivedAt().compareTo(a1.getReceivedAt()))
                .map(analyticsEventMapper::toResponseAnalyticsEventDto)
                .toList();
    }

    private Stream<AnalyticsEvent> filterEventsByInterval(Stream<AnalyticsEvent> events, Interval interval) {
        return events
                .filter(event -> event.getReceivedAt()
                        .isAfter(interval.getStart()) && event.getReceivedAt()
                        .isBefore(interval.getEnd()));
    }

    private Stream<AnalyticsEvent> filterEventsManually(
            Stream<AnalyticsEvent> events,
            LocalDateTime from,
            LocalDateTime to) {
        return events
                .filter(event -> !event.getReceivedAt()
                        .isBefore(from) && !event.getReceivedAt()
                        .isAfter(to));
    }
}
