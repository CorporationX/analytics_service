package faang.school.analytics.mapper;

import faang.school.analytics.dto.MessageEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(MessageEvent messageEvent);
}
