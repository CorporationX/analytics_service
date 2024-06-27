package faang.school.analytics.mapper.premium;

import faang.school.analytics.event.premium.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PremiumBoughtEventMapper {

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "boughtAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEvent premiumBoughtEvent);
}
