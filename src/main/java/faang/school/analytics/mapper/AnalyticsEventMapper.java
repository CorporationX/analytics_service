package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.BeforeMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {

    AnalyticsEvent toModel(AnalyticsEventDto eventDto);

    AnalyticsEventDto toDto(AnalyticsEvent event);

    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "postId", target = "receiverId")
    @Mapping(source = "createdAt", target = "receivedAt")
    AnalyticsEventDto toAnalyticsEventDto(CommentEventDto commentEventDto);
}