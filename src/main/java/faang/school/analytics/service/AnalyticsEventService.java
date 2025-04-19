package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;

public interface AnalyticsEventService {
    List<AnalyticsEvent> getAnalytics(Long receiverId, String eventTypeRaw, String interval, String startDate,
                                      String endDate);
}
