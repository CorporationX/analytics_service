package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования сущности {@link AnalyticsEvent} в DTO {@link AnalyticsEventDto}.
 */
@Mapper(componentModel = "spring")
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);
}
