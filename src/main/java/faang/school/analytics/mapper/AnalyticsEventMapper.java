package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "subscriptionTime", target = "receivedAt")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "followerId", target = "actorId")
    AnalyticsEvent toEntity(FollowerEventDto followerEventDto);

    @Mapping(source = "recommendationId", target = "id")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent recomendationEventToAnalyticsEvent(RecommendationEvent recommendationEvent);

    @Mapping(source = "requesterId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent mentorshipRequestedEventToAnalyticsEvent(MentorshipRequestedEvent mentorshipRequestedEvent);
}
