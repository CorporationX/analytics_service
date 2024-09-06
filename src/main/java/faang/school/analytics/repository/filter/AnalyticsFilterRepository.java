package faang.school.analytics.repository.filter;

import faang.school.analytics.dto.event.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;

public interface AnalyticsFilterRepository {

    boolean isApplicable(AnalyticsFilterDto analyticsFilterDto);

    Specification<AnalyticsEvent> apply(AnalyticsFilterDto analyticsFilterDto);
}
