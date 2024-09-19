package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.event.PostViewEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostViewEventMapper extends GenericEventMapper<PostViewEventDto, AnalyticsEventDto> {

    @Override
    @Mapping(source = "authorId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(target = "eventType", constant = "POST_VIEW")
    AnalyticsEventDto toEntity(PostViewEventDto postViewEventDto);
}
