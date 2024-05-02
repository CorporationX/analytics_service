package faang.school.analytics.mapper;

public interface AnalyticsMapper<E, A> {
    A toAnalyticsEvent(E event);
}
