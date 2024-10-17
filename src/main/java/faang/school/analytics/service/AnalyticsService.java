package faang.school.analytics.service;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;

public interface AnalyticsService {
    List<AnalyticsEvent> getAnalytics(long receiverId, String eventTypeString, Integer eventTypeInteger,
                                             String intervalString, Integer intervalInteger, LocalDateTimeInput startDate,
                                             LocalDateTimeInput endDate);
}
