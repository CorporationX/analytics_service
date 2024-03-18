package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
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
        AnalyticsEvent savedAnalyticsEvent = analyticsEventRepository.save(event);
        log.info("Event successful saved {}", savedAnalyticsEvent);
    }

    public void deleteEvent(long id) {
        analyticsEventRepository.deleteById(id);
        log.info("Event successful deleted");
    }

    public List<AnalyticsEvent> getAnalytics(long id, EventType type) {
        return analyticsEventRepository.findByReceiverIdAndEventType(id, type).toList();
    }

    public AnalyticsEvent getById(long id) {
        String exceptionMessage = String.format("Analytics event with id = %s not found", id);
        return analyticsEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}