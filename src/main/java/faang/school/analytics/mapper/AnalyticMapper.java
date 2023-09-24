package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticResponseDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticMapper {
    AnalyticResponseDto toResponseDto(AnalyticsEvent event);

    default List<AnalyticResponseDto> toDtoList(List<AnalyticsEvent> events) {
        return events.stream().map(this::toResponseDto).toList();
    }
}