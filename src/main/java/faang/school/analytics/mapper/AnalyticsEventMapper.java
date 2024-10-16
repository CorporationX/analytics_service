package faang.school.analytics.mapper;

import faang.school.analytics.listener.event.ProfileVeiwEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.listener.event.SearchAppearanceEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEvent toEntity(SearchAppearanceEvent searchAppearanceEvent, String eventType);

    AnalyticsEvent toEntity(ProfileVeiwEvent profileVeiwEvent, String eventType);
}
