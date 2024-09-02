package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author Evgenii Malkov
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent entity);

    AnalyticsEvent toEntity(AnalyticsEventDto dto);

    List<AnalyticsEventDto> toDtoList(List<AnalyticsEvent> analyticsEventList);
}
