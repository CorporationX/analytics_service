package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostViewEventMapper {

    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "receivedAt", source = "viewTime")
    AnalyticsEvent toEntity(PostViewEventDto dto);

    PostViewEventDto toDto(AnalyticsEvent event);
}
