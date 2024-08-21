package faang.school.analytics.repository.filter;

import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReceiverIdFilterSpecification implements AnalyticsFilterRepository {

    @Override
    public boolean isApplicable(AnalyticsFilterDto filter) {
        return filter.getReceiverId() != null;
    }

    @Override
    public Specification<AnalyticsEvent> apply(AnalyticsFilterDto filter) {
        return (root, query, builder) ->
                builder.equal(root.get("receiverId"), filter.getReceiverId());
    }
}
