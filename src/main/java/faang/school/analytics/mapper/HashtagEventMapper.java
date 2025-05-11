package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.HashtagRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HashtagEventMapper {

    @Mapping(target = "receiverId", source = "userId")
    @Mapping(target = "actorId", source = "hashtagId")
    @Mapping(target = "eventType", expression = "java(faang.school.analytics.model.EventType.HASHTAG_REQUESTED)")
    AnalyticsEventDto toDto(HashtagRequestEvent event);
}
