package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    public final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEvent addAnalyticsEvent(AnalyticsEvent analyticsEvent) {
        return analyticsEventRepository.save(analyticsEvent);
    }

}
