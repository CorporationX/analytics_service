package faang.school.analytics.mapper;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventMapper<T> {
    AnalyticsEvent toAnalyticsEvent(T t);

    Class<T> getType ();
}
