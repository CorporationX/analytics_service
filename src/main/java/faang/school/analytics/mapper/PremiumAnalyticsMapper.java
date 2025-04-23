package faang.school.analytics.mapper;

import faang.school.analytics.dto.premium.PremiumAnalyticsDto;
import faang.school.analytics.model.PremiumAnalytics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PremiumAnalyticsMapper {
    PremiumAnalytics toPremiumAnalytics(PremiumAnalyticsDto premiumAnalyticsDto);
}
