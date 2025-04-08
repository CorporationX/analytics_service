package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventFromDateFilter implements AnalyticsEventFilter {

    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filter) {
        return filter.from() != null;
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsEventFilterDto filter) {
        return ((root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("receivedAt"), filter.from()));
    }
}
