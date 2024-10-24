package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.type.service.post.like.PostLikeEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "userExciterId")
    @Mapping(target = "receivedAt", source = "createdAt")
    AnalyticsEvent toAnalyticsEvent(PostLikeEventDto postLikeEventDto);

    @Mapping(target = "authorId", source = "receiverId")
    @Mapping(target = "userExciterId", source = "actorId")
    @Mapping(target = "createdAt", source = "receivedAt")
    PostLikeEventDto toPostLikeEventDto(AnalyticsEventDto analyticsEventDto);

}
