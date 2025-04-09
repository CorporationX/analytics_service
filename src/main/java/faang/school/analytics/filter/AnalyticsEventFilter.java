package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.domain.Specification;

public interface AnalyticsEventFilter {

    boolean isApplicable(AnalyticsEventFilterDto filter);

    Specification<AnalyticsEvent> apply(AnalyticsEventFilterDto filter);
}
