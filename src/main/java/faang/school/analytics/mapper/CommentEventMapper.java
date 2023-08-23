package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentEventMapper {

    @Mapping(source = "post.id", target = "receiverId")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "publishedAt", target = "receivedAt")
    AnalyticsEvent toEntity(CommentEventDto eventDto);
}