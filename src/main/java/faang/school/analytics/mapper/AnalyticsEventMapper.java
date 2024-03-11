package faang.school.analytics.mapper;

import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(target = "actorId", expression = "java(faang.school.analytics.model.EventType.PREMIUM_BOUGHT.ordinal())")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.PREMIUM_BOUGHT)")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent event);

}
