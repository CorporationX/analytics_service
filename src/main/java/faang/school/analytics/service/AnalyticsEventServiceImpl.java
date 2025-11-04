package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventServiceImpl implements AnalyticsEventService{

    private final AnalyticsEventRepository repository;

    @Override
    public void save(AnalyticsEvent analyticsEvent) {
        repository.save(analyticsEvent);
    }
}
