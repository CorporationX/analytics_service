package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalylticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
     AnalylticsEventDto toDto(AnalyticsEvent entity);
}
