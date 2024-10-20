package faang.school.analytics.service;

import faang.school.analytics.model.SearchAppearanceEvent;

public interface AnalyticsEventService {
    void saveSearchAppearanceEvent(SearchAppearanceEvent analyticsEvent);
}
