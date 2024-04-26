package faang.school.analytics.mappers;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public  abstract class AbstractAnalyticsEventMapper<T>{
    private final AnalyticsEventMapper<T> AnalyticsMapper;

    abstract public boolean isSupported(Class<?> type);

    public AnalyticsEvent handleMapping(T event, EventType type){
        AnalyticsEvent analyticsEvent = AnalyticsMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(type);
        return analyticsEvent;
    }
}
