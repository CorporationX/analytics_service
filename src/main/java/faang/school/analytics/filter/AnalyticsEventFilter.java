package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public interface AnalyticsEventFilter {
    boolean isApplicable(AnalyticsEventFilterDto filterDto);

    Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> analyticsEvents, AnalyticsEventFilterDto filterDto);
}
