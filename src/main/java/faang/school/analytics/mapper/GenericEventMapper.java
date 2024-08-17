package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import faang.school.analytics.model.AnalyticsEvent;


public interface GenericEventMapper<T, E extends AnalyticsEventDto> {
    E toEntity(T event);
}
