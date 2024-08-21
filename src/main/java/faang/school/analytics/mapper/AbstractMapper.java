package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.AnalyticsEventDto;

public interface AbstractMapper<T> {

    AnalyticsEventDto toAnalyticsEventDto(T type);
}
