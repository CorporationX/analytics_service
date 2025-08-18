package faang.school.analytics.service;

import faang.school.analytics.event.AnaliticsEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class AnaliticsEventService {
    public AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
    }

    public AnaliticsEvent getAnalitics(long receiverId, EventType eventType, Interval interval,
                                       LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> event = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return null;
    }
}
