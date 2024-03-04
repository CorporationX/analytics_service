package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService<T> {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper<T> analyticsEventMapper;

    public void saveEvent(T event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Event successful saved");
    }

    public void deleteEvent(long id) {
        analyticsEventRepository.deleteById(id);
        log.info("Event successful deleted");
    }

    public Stream<AnalyticsEvent> getAnalytics(long id, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(id, type);
    }

}
