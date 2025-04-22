package faang.school.analytics.service;

import faang.school.analytics.dto.AggregatedAnalyticDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsGetDto;
import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsEventService {

    AnalyticsEventDto saveAnalytics(AnalyticsEventDto analyticsEventDto);

    List<AggregatedAnalyticDto> getAnalytics(AnalyticsGetDto analyticsGetDto);
}
