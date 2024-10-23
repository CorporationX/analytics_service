package faang.school.analytics.mapper;

import faang.school.analytics.dto.AdBoughtEvent;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.listener.event.ProfileVeiwEvent;
import faang.school.analytics.listener.event.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "authorPostId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_LIKE")
    @Mapping(target = "receivedAt", expression = "java(java.time.LocalDateTime.now())")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

    AnalyticsEvent toEntity(SearchAppearanceEvent searchAppearanceEvent, String eventType);

    AnalyticsEvent toEntity(ProfileVeiwEvent profileVeiwEvent, String eventType);

    AnalyticsEvent toEntity(AdBoughtEvent adBoughtEvent, String eventType);
}
