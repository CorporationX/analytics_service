package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsCreateEventDto;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;

public interface AnalyticsService {

    AnalyticsEventDto saveEvent(AnalyticsCreateEventDto analyticsCreateEventDto);

    List<AnalyticsEventDto> getAnalytics(GetAnalyticsRqDto getAnalyticsRqDto);

    AnalyticsEvent addAnalyticsEvent(AnalyticsEvent analyticsEvent);
}
