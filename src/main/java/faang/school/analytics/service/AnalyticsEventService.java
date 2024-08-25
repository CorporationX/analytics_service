package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.AnalyticsEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventValidator analyticsEventValidator;

    @Transactional(readOnly = true)
    public Stream<AnalyticsEvent> getEvents(long receiverId, String eventType, String interval,
                                            String startTime, String endTime) {
        EventType type = EventType.getType(eventType);
        analyticsEventValidator.validateIntervalOrPeriod(interval, startTime, endTime);
        LocalDateTime from;
        LocalDateTime to;
        if (interval != null) {
            Interval i = Interval.getInterval(interval);
            from = Interval.getStartDateTimeByInterval(i);
            to = LocalDateTime.now();
        } else {
            from = LocalDateTime.parse(startTime);
            to = LocalDateTime.parse(endTime);
        }
        return analyticsEventRepository.findAllByReceiverIdAndEventTypeAndReceivedAtIsBetween(receiverId, type, from, to);
    }

    @Transactional
    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }
}
