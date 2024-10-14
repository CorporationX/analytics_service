package faang.school.analytics.filter.analyticseventfilter;

import faang.school.analytics.model.dto.event.AnalyticsEventFilterDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AnalyticsPeriodFilter implements AnalyticsEventFilter {
    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filters) {
        return filters.interval() == null && filters.from() != null && filters.to() != null;
    }

    @Override
    public Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> eventStream, AnalyticsEventFilterDto filters) {
        return eventStream.filter(event ->
                event.getReceivedAt().isAfter(filters.from()) && event.getReceivedAt().isBefore(filters.to()));
    }
}
