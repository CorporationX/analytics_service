package faang.school.analytics.repository.filter;

import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TimeRangeFilterSpecification implements AnalyticsFilterRepository {

    @Override
    public boolean isApplicable(AnalyticsFilterDto filter) {
        return filter.getFrom() != null && filter.getTo() != null && filter.getTo().isAfter(filter.getFrom());
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsFilterDto filter) {
        return (root, query, builder) -> builder.between(root.get("receivedAt"), filter.getFrom(), filter.getTo());
    }
}
