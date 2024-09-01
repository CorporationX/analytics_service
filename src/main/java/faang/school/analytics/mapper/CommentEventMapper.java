package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CommentEventMapper {

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(target = "eventType", expression = "java(setEventType())")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEventDto toAnalyticsEventDto(CommentEvent commentEvent);

    default String setEventType(){
        return "POST_COMMENT";
    }
}
