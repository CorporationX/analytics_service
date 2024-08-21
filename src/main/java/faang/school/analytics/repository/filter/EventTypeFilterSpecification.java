package faang.school.analytics.repository.filter;

import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EventTypeFilterSpecification implements AnalyticsFilterRepository {

    @Override
    public boolean isApplicable(AnalyticsFilterDto filter) {
        return filter.getEventType() != null;
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsFilterDto filter) {
        return (root, query, builder) -> builder.equal(root.get("eventType"), filter.getEventType());
    }
}
