package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }
}
