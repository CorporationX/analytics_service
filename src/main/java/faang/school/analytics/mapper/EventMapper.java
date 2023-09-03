package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.followEvent.FollowEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    @Mapping(target = "receiverId", source = "authorId")
    @Mapping(target = "actorId", source = "commentId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType. POST_COMMENT)")
    AnalyticsEvent toEntity(CommentEventDto commentEventDto);

    @Mapping(source = "subscriberId", target = "actorId")
    @Mapping(source = "targetUserId", target = "receiverId")
    @Mapping(source = "subscriptionDateTime", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)")
    AnalyticsEvent toEntity(FollowEventDto dto);

    AnalyticsDto toDto(AnalyticsEvent event);

    List<AnalyticsDto> toDtos(List<AnalyticsEvent> event);
}
