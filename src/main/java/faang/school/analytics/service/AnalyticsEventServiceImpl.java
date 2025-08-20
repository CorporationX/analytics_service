package faang.school.analytics.service;

import faang.school.analytics.event.EventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class AnalyticsEventServiceImpl implements AnalyticsEventService {
    public AnalyticsEventRepository analyticsEventRepository;
    public AnalyticsEventMapper analyticsEventMapper;

    AnalyticsEventServiceImpl(AnalyticsEventRepository analyticsEventRepository, AnalyticsEventMapper analyticsEventMapper) {
        this.analyticsEventRepository = analyticsEventRepository;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    public void saveEvent(EventDto eventDto) {

        analyticsEventRepository.save(analyticsEvent);
    }

    public EventDto getAnalitics(long receiverId, EventType eventType, Interval interval,
                                 LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> event = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        return null;
    }
}
