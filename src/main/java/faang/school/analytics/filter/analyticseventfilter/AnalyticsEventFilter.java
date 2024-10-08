package faang.school.analytics.filter.analyticseventfilter;

import faang.school.analytics.dto.analyticsevent.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.stream.Stream;

public interface AnalyticsEventFilter {

    boolean isApplicable(AnalyticsEventFilterDto filters);

    Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> eventStream, AnalyticsEventFilterDto filters);
}
