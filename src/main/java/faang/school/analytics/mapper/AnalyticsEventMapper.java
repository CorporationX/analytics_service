package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(PostViewEvent postViewEvent);

    PostViewEvent toDto(AnalyticsEvent analyticsEvent);
}
