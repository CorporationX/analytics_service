package faang.school.analytics.mapper;

import faang.school.analytics.dto.interval.IntervalDto;
import faang.school.analytics.model.Interval;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface IntervalMapper {
    IntervalDto toDto(Interval interval);
    Interval toEntity(IntervalDto intervalDto);
}
