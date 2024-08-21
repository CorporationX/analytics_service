package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.PostViewEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostViewMapper extends AbstractMapper<PostViewEvent> {

    @Override
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(this.mapEventType())")
    AnalyticsEventDto toAnalyticsEventDto(PostViewEvent event);

    default EventType mapEventType() {
        return EventType.POST_VIEW;
    }
}
