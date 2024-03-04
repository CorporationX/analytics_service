package faang.school.analytics.mapper;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Alexander Bulgakov
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toAnalyticsEvent(SearchAppearanceEvent event);
}
