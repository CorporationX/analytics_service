package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Event successful saved {}", event);
    }

    public void deleteEvent(long id) {
        analyticsEventRepository.deleteById(id);
        log.info("Event successful deleted");
    }

    public List<AnalyticsEvent> getAnalytics(long id, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(id, type).toList();
    }

}
