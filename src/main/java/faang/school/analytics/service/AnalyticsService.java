package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;

public interface AnalyticsService {

    AnalyticsEventDto save(AnalyticsEventDto event);
}
