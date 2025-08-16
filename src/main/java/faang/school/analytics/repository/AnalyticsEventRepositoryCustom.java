package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.criteria.AnalyticsGetCriteria;

import java.util.List;

public interface AnalyticsEventRepositoryCustom {
    List<AnalyticsEvent> findByCriteria(AnalyticsGetCriteria criteria);
}