package faang.school.analytics.service.premium;

import faang.school.analytics.dto.premium.PremiumAnalyticsDto;
import faang.school.analytics.mapper.PremiumAnalyticsMapper;
import faang.school.analytics.model.PremiumAnalytics;
import faang.school.analytics.repository.PremiumAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PremiumAnalyticsServiceImpl implements PremiumAnalyticsService {
    private final PremiumAnalyticsRepository premiumAnalyticsRepository;
    private final PremiumAnalyticsMapper premiumAnalyticsMapper;

    @Override
    public void savePremiumAnalytics(PremiumAnalyticsDto premiumAnalyticsDto) {
        PremiumAnalytics premiumAnalytics = premiumAnalyticsMapper.toPremiumAnalytics(premiumAnalyticsDto);
        premiumAnalyticsRepository.save(premiumAnalytics);
    }
}
