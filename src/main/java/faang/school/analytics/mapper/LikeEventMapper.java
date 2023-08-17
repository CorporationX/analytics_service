package faang.school.analytics.mapper;

import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LikeEventMapper {

    LikeEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent toModel(LikeEventDto likeDto);
}
