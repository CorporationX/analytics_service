package faang.school.analytics.mapper;

import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> events);

    AnalyticsEvent toAnalyticsEvent(ProfileViewEventDto analyticsEventService);

    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "purchaseDate", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(PremiumBoughtEventDto premiumBoughtEventDto);

    List<AnalyticsEvent> profileViewToAnalyticsEvents(List<ProfileViewEventDto> profileViewEventDtos);

    List<AnalyticsEvent> premiumBoughtToAnalyticsEvents(List<PremiumBoughtEventDto> premiumBoughtEventDtos);
}
