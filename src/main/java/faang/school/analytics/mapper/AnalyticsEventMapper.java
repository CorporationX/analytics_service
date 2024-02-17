package faang.school.analytics.mapper;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    AnalyticsEvent toEntity(FollowerEventDto followerEventDto);
}
