package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipEventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentorshipEventMapper {

    MentorshipEventDto toDto(MentorshipEventDto event);

    MentorshipEventDto toEntity(MentorshipEventDto dto);
}