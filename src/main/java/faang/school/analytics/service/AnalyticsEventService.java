package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;

    public AnalyticsEventService(AnalyticsEventRepository repository) {
        this.repository = repository;
    }

    public void saveEvent(AnalyticsEvent event) {
        repository.save(event);
    }
}