package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository repository;

    public void processCommentEvent(AnalyticsEvent analyticsEvent) {
        repository.save(analyticsEvent);
    }

    public List<AnalyticsEvent> getAllEvents() {
        return (List<AnalyticsEvent>) repository.findAll();
    }
}