package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MentorshipEventMapper {

    MentorshipEventMapper INSTANCE = Mappers.getMapper(MentorshipEventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actorId", source = "senderId")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "eventType", constant = "PROJECT_INVITE")
    @Mapping(target = "receivedAt", source = "timestamp")
    AnalyticsEvent toEntity(MentorshipEventDto dto);

    @Mapping(target = "senderId", source = "actorId")
    @Mapping(target = "receiverId", source = "receiverId")
    @Mapping(target = "timestamp", source = "receivedAt")
    MentorshipEventDto toDto(AnalyticsEvent entity);
}