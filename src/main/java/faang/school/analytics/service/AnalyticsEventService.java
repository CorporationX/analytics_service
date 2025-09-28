package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.transaction.annotation.Transactional;

public interface AnalyticsEventService {
    @Transactional
    AnalyticsEvent save();
}
