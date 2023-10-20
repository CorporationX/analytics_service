package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.ProjectViewDto;
import faang.school.analytics.listener.ProjectViewEventListener;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectViewEventMapper {

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "dateTime", target = "receivedAt")
    EventDto toEventDto(ProjectViewDto projectViewDto);
}
