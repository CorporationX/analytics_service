package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Transactional
    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<AnalyticEventDto> getAnalytics(long receiverId,
                                               EventType eventType,
                                               Interval interval,
                                               LocalDateTime from,
                                               LocalDateTime to) {

        Stream<AnalyticsEvent> streamByReceiverIdAndEventType =
                analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return streamByReceiverIdAndEventType
                .filter(analyticsEvent -> interval == null ?
                        filterBetweenDate(from, to, analyticsEvent.getReceivedAt()) :
                        filterByIntervalDays(interval, analyticsEvent.getReceivedAt()))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(analyticsEventMapper::toDto)
                .toList();
    }

    private boolean filterBetweenDate(LocalDateTime from, LocalDateTime to, LocalDateTime dateTime) {
        return dateTime.isAfter(from) && dateTime.isBefore(to);
    }

    private boolean filterByIntervalDays(Interval interval, LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now().minusDays(interval.getDays()));
    }
}
