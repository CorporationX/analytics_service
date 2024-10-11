package faang.school.analytics.mapper.event;

import faang.school.analytics.dto.event.like.LikeEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeEventMapper {
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "likerId", target = "actorId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEvent fromLikeEventToEntity(LikeEventDto likeEventDto);
}
