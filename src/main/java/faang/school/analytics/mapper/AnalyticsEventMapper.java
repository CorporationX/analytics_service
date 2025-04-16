package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    @Mapping(target = "receivedAt", source = "timestamp")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "eventType", ignore = true)
    AnalyticsEvent toAnalyticsEvent(CommentEvent commentEvent);
}
