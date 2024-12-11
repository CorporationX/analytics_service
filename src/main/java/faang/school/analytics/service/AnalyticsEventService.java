package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.mapper.IntervalMapProvider;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final IntervalMapProvider intervalMapProvider;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(long receiverId,
                                                EventType eventType,
                                                Interval interval,
                                                LocalDateTime from,
                                                LocalDateTime to) {

        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return events
                .filter(event -> filterEvent(event, interval, from, to))
                .sorted((e1, e2) -> e2.getReceivedAt().compareTo(e1.getReceivedAt()))
                .map(analyticsEventMapper::toDto)
                .collect(Collectors.toList());
    }

    private boolean filterEvent(AnalyticsEvent event, Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            return event.getReceivedAt().isAfter(intervalMapProvider.getIntervalMap().get(interval));
        } else {
            return event.getReceivedAt().isAfter(from) && event.getReceivedAt().isBefore(to);
        }
    }
}
