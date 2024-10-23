package faang.school.analytics.service;

import faang.school.analytics.dto.AdBoughtEvent;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.listener.event.ProfileVeiwEvent;
import faang.school.analytics.listener.event.SearchAppearanceEvent;

public interface AnalyticsEventService {

    void saveLikeEvent(LikeEvent event);

    AnalyticsEvent getAnalyticOfEvent(Long entityId,
                                             Long eventTypeId,
                                             Long intervalId,
                                             String startDateTime,
                                             String endDateTime);
    void saveSearchAppearanceEvent(SearchAppearanceEvent analyticsEvent);

    void saveProfileViewEvent(ProfileVeiwEvent analyticsEvent);

    void saveAdBoughtEvent(AdBoughtEvent analyticsEvent);
}
