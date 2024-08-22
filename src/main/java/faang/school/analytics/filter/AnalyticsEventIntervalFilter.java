package faang.school.analytics.filter;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class AnalyticsEventIntervalFilter {

    public Stream<AnalyticsEvent> filter(Stream<AnalyticsEvent> analyticsEventStream, Interval interval) {
        return analyticsEventStream.filter(analyticsEvent -> analyticsEvent.getReceivedAt()
                .plus(interval.getPeriod())
                .isAfter(LocalDateTime.now()));
    }
}
