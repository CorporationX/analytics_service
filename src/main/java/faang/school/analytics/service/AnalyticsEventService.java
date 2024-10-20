package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {

    void saveEvent(AnalyticsEvent analyticsEvent);

    AnalyticsEvent getAnalyticOfEvent(Long entityId,
                                             Long eventTypeId,
                                             Long intervalId,
                                             String startDateTime,
                                             String endDateTime);

}
