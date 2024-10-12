package faang.school.analytics.mapper;

import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "purchaseDate", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEventDto premiumBoughtEventDto);

    List<AnalyticsEvent> toAnalyticsEvents(List<PremiumBoughtEventDto> premiumBoughtEventDtos);
}
