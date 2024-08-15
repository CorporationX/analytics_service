package faang.school.analytics.mapper;

import faang.school.analytics.dto.postview.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PostViewEventMapper {
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "userId", target = "receiverId")
    @Mapping(source = "timestamp", target = "receivedAt")
    AnalyticsEvent toEntity(PostViewEventDto postViewEventDto);
}
