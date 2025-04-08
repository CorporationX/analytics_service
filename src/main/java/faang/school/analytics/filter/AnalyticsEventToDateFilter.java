package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventToDateFilter implements AnalyticsEventFilter {

    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filter) {
        return filter.to() != null;
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsEventFilterDto filter) {
        return ((root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("receivedAt"), filter.to()));
    }
}
