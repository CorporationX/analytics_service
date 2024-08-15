package faang.school.analytics.filter.impl;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilter;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsIntervalFilter implements AnalyticsFilter {
    @Override
    public boolean isApplicable(AnalyticsFilterDto dto) {
        return dto.getInterval() != null;
    }

    @Override
    public boolean action(AnalyticsEvent event, AnalyticsFilterDto dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = event.getReceivedAt();
        Interval interval = dto.getInterval();
        switch (interval) {
            case HOURLY:
                return date.isAfter(now.minusHours(1));
            case DAILY:
                return date.isAfter(now.minusDays(1));
            case WEEKLY:
                return date.isAfter(now.minusWeeks(1));
            case MONTHLY:
                return date.isAfter(now.minusMonths(1));
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }

    }
}
