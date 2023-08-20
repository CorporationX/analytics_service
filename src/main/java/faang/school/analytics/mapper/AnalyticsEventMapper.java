package faang.school.analytics.mapper;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnalyticsEventMapper {

    EventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent toModel(EventDto likeDto);
}
