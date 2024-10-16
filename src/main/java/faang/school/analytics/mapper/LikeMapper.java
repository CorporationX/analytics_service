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

    @Mapping(target = "receiverId", source = "likedEntityId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receivedAt", source = "createdAt")
    @Mapping(target = "eventType", expression = "java(mapEventType(postLikeEventDto))")
    AnalyticsEvent toAnalyticsEvent(PostLikeEventDto postLikeEventDto);

    @Mapping(target = "likedEntityId", source = "receiverId")
    @Mapping(target = "authorId", source = "actorId")
    @Mapping(target = "createdAt", source = "receivedAt")
    PostLikeEventDto toPostLikeEventDto(AnalyticsEventDto analyticsEventDto);


    default EventType mapEventType(PostLikeEventDto postLikeEventDto) {
        return EventType.fromEvent(postLikeEventDto);
    }
}
