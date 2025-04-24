package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticDto;
import faang.school.analytics.dto.subscription.FollowerEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", source = "followerId")
    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "createdAt", source = "timestamp")
    AnalyticDto toAnalyticDto(FollowerEventDto followerEventDto);
}