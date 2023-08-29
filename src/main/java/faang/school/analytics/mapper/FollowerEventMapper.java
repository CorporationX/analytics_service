package faang.school.analytics.mapper;

import faang.school.analytics.dto.redis.FollowerEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowerEventMapper {

    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toEntity(FollowerEventDto event);

    @Mapping(source = "receiverId", target = "followeeId")
    @Mapping(source = "actorId", target = "followerId")
    @Mapping(source = "receivedAt", target = "timestamp")
    FollowerEventDto toDto(AnalyticsEvent entity);

}
