package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventIntervalFilter implements AnalyticsEventFilter {

    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filter) {
        return filter.interval() != null;
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsEventFilterDto filter) {
        return ((root, query, builder) -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fromTime = now.minusSeconds(filter.interval().getSeconds());
            return builder.greaterThanOrEqualTo(root.get("receivedAt"), fromTime);
        });
    }
}
