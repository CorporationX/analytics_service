package faang.school.analytics.filter.analyticseventfilter;

import faang.school.analytics.model.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.entity.AnalyticsEvent;

import java.util.stream.Stream;

public interface AnalyticsEventFilter {

    boolean isApplicable(AnalyticsEventFilterDto filters);

    Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> eventStream, AnalyticsEventFilterDto filters);
}
