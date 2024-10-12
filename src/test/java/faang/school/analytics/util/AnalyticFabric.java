package faang.school.analytics.util;

import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;
import java.util.stream.LongStream;

public class AnalyticFabric {
    public static AnalyticsEvent buildAnalyticsEvent(Long id) {
        return AnalyticsEvent
                .builder()
                .id(id)
                .build();
    }

    public static List<AnalyticsEvent> buildAnalyticsEvents(long idStart, long idEnd) {
        return LongStream
                .rangeClosed(idStart, idEnd)
                .mapToObj(AnalyticFabric::buildAnalyticsEvent)
                .toList();
    }
}
