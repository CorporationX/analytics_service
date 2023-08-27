package faang.school.analytics.mapper;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FollowerEventMapper {

   @Mapping(target = "actorId", source = "followerId")
   @Mapping(target = "receiverId", source = "followeeId")
   @Mapping(target = "receivedAt", source = "subscriptionTime")
   AnalyticsEvent toModel(FollowerEventDto eventDto);

   @Mapping(target = "actorId", source = "followerId")
   @Mapping(target = "receiverId", source = "followeeId")
   @Mapping(target = "receivedAt", source = "subscriptionTime")
   EventDto toDto(FollowerEventDto eventDto);
}