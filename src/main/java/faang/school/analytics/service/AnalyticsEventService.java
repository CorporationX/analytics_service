package faang.school.analytics.service;

import faang.school.analytics.listener.event.ProfileVeiwEvent;
import faang.school.analytics.listener.event.SearchAppearanceEvent;

public interface AnalyticsEventService {

    void saveSearchAppearanceEvent(SearchAppearanceEvent analyticsEvent);

    void saveProfileViewEvent(ProfileVeiwEvent analyticsEvent);
}
