package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.stream.Stream;

public interface AnalyticsFilterI {
    boolean isApplicable(AnalyticsFilterDto analyticsFilterDto);
    Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> eventsStream, AnalyticsFilterDto analyticsFilterDto);
}
