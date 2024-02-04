package faang.school.analytics.service;

import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.mapper.PremiumEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PremiumEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final PremiumEventMapper premiumEventMapper;

    public void save(PremiumEvent premiumEvent) {
        AnalyticsEvent analyticsEvent = premiumEventMapper.toAnalyticsEvent(premiumEvent);
        analyticsEventRepository.save(analyticsEvent);
    }
}
