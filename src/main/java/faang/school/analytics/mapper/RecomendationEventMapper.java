package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecomendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecomendationEventMapper {

    @Mapping(source = "recomendationId", target = "id")
    @Mapping(source = "authorId", target = "actorId")
    @Mapping(source = "dateTime", target = "receivedAt")
    AnalyticsEvent toEntity(RecomendationEventDto recomendationEventDto);


}
