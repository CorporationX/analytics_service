package faang.school.analytics.filter;

import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsFilter {

    boolean isApplicable(AnalyticsFilterDto dto);

    boolean test(AnalyticsEvent event, AnalyticsFilterDto dto);
}
