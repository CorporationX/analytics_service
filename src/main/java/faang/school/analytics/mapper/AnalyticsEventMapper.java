package faang.school.analytics.mapper;

import faang.school.analytics.dto.redis.AnalyticsEventDto;
import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receiverId", source = "authorId")
    AnalyticsEvent toEntity(PostViewEventDto event);

    @Mapping(target = "createdAt", source = "receivedAt")
    @Mapping(target = "userId", source = "actorId")
    @Mapping(target = "authorId", source = "receiverId")
    PostViewEventDto toDto(AnalyticsEvent entity);
}
