package faang.school.analytics.mapper;

import faang.school.analytics.dto.AbstractEventDto;
import faang.school.analytics.model.AnalyticsEvent;

public interface EventMapper {
    AnalyticsEvent toEntity(AbstractEventDto abstractEventDto);
}