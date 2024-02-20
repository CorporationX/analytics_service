package faang.school.analytics.mapper.base;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventMapper<T> {
    AnalyticsEvent toAnalyticsEvent(T t);

    Class<T> getType ();
}
