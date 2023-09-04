package faang.school.analytics.mapper;

import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "recommendationId", target = "receiverId")
    AnalyticsEvent toEntity(RecommendationEventDto dto);
}