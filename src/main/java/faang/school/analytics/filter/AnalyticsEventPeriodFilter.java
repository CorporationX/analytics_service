package faang.school.analytics.filter;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class AnalyticsEventPeriodFilter {

    public Stream<AnalyticsEvent> filter(Stream<AnalyticsEvent> analyticsEventStream,
                                         LocalDateTime from, LocalDateTime to) {
        return analyticsEventStream
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(from))
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(to));

    }
}
