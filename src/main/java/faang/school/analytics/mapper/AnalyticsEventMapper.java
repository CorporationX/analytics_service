package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestEvent;

import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "recommendationId", target = "receiverId")
    AnalyticsEvent toEntity(RecommendationEventDto dto);
    AnalyticsEvent toEntity(MentorshipRequestEvent dto);

    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "receivedAt", source = "subscriptionTime")
    AnalyticsEvent toEntity(FollowerEventDto eventDto);

    @Mapping(source = "commentId", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    AnalyticsEvent toEntity(CommentEventDto dto);
}