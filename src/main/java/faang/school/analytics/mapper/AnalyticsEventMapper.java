package faang.school.analytics.mapper;

import faang.school.analytics.dto.LikePostEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {EventType.class})
public interface AnalyticsEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiverId", source = "authorPostId")
    @Mapping(target = "actorId", source = "likedUserId")
    @Mapping(target = "eventType", expression = "java(EventType.POST_LIKE)")
    @Mapping(target = "receivedAt", source = "likeTime")
    AnalyticsEvent toAnalyticsLikePostEvent(LikePostEventDto likePostEventDto);

}
