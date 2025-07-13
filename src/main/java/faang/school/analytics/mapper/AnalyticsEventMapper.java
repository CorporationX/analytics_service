package faang.school.analytics.mapper;

import faang.school.analytics.kafka.events.RecommendationEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "recipientId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent fromEvent(RecommendationEvent event);
}
