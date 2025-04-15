package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.PostViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostViewEventMapper {

    @Mapping(source = "postId",target = "actorId")
    @Mapping(source = "userId",target = "receiverId")
    @Mapping(source = "date",target = "receivedAt")
    @Mapping(target = "eventType", constant = "POST_VIEW")
    AnalyticsEventDto postViewToAnalyticEventDto(PostViewEvent postViewEvent);
}
