package faang.school.analytics.service;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.request.GetAnalyticsRequestDto;
import lombok.NonNull;

import java.util.List;

public interface AnalyticsEventService {
    AnalyticsEventDto saveEvent(@NonNull AnalyticsEventDto analyticsEventDto);

    List<AnalyticsEventDto> getAnalytics(@NonNull GetAnalyticsRequestDto getAnalyticsRequestDto);
}
