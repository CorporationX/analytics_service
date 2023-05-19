package faang.school.analytics.mapper;

import faang.school.analytics.dto.UserProfileViewDto;
import faang.school.analytics.dto.event.UserProfileViewEvent;
import faang.school.analytics.model.UserProfileView;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileViewMapper {

    UserProfileView toEntity(UserProfileViewEvent event);

    UserProfileViewDto toDto(UserProfileView entity);
}
