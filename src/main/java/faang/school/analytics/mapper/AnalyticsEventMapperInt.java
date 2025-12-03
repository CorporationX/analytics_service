package faang.school.analytics.mapper;

import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapperInt {

    @Mapping(target = "receivedAt", source = "viewTime")
    AnalyticsEvent toAnalyticsEvent(SearchAppearanceEvent searchAppearanceEvent);
}