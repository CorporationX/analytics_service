package faang.school.analytics.filter.impl;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilter;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsDateRangeFilter implements AnalyticsFilter {
    @Override
    public boolean isApplicable(AnalyticsFilterDto dto) {
        return dto.getFrom() != null && dto.getTo() != null;
    }

    @Override
    public boolean test(AnalyticsEvent event, AnalyticsFilterDto dto) {
        LocalDateTime time = event.getReceivedAt();
        return time.isAfter(dto.getFrom()) && time.isBefore(dto.getTo());
    }
}
