package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalylticsEventDto;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalylticsEventDto toDto(AnalyticsEvent entity);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    @Mapping(target = "eventType", constant = "PREMIUM_BOUGHT")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent event);
}
