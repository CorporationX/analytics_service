package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumBoughtMapper {
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEventDto eventDto);

    PremiumBoughtEventDto toPremiumBoughtEventDto(AnalyticsEvent event);
}
