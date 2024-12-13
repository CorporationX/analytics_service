package faang.school.analytics.service.events;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class AnalyticsEventFilter {

    public Stream<AnalyticsEvent> filterByInterval(Stream<AnalyticsEvent> events, int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        return events
                .filter(e -> e.getReceivedAt() != null && from.isBefore(e.getReceivedAt()));
    }

    public Stream<AnalyticsEvent> filterByDates(Stream<AnalyticsEvent> events, LocalDateTime from, LocalDateTime to) {
        return events
                .filter(e -> isDateBetween(e.getReceivedAt(), from, to));
    }

    public boolean isDateBetween(LocalDateTime date, LocalDateTime from, LocalDateTime to) {
        if (date == null) {
            return false;
        }
        if (from != null && from.isAfter(date)) {
            return false;
        }
        if (to != null && to.isBefore(date)) {
            return false;
        }
        return true;
    }

}
