package faang.school.analytics.service;

import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PremiumEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void save(PremiumEvent premiumEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(premiumEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
