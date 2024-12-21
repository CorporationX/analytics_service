package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.event.AdBoughtEvent;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.MentorshipRequestEvent;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followeeId", target = "receiverId")
    @Mapping(source = "subscribedAt", target = "receivedAt")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.FOLLOWER)")
    AnalyticsEvent toEntity(SubscriptionEvent event);

    default AnalyticsEvent dtoToEntity(AdBoughtEvent event) {
        return AnalyticsEvent.builder()
                .id(event.getPostId())
                .actorId(event.getActorId())
                .receiverId(event.getReceiverId())
                .paymentAmount(event.getPaymentAmount())
                .adDuration(event.getAdDuration())
                .receivedAt(event.getReceivedAt())
                .build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "time", target = "receivedAt")
    AnalyticsEvent toAnalyticsEventMentorshipRequest(MentorshipRequestEvent mentorshipRequestEvent);
    AnalyticsEvent toEntity(AnalyticsEventResponseDto dto);

    @Mapping(source = "postAuthorId", target = "receiverId")
    @Mapping(source = "commentAuthorId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.POST_COMMENT)")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent newCommentEventToEntity(CommentEvent commentEvent);

    @Mapping(source = "goalId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.GOAL_COMPLETED)")
    @Mapping(source = "completedAt", target = "receivedAt")
    AnalyticsEvent goalCompletedEventToEntity(GoalCompletedEvent dto);
}
