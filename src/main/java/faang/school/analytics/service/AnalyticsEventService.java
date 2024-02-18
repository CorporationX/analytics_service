package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;


    public AnalyticsEvent saveAnalyticsEvent (AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }
}