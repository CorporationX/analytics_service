package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.AnalyticsPremiumBoughtEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsPremiumBoughtEventRepository extends CrudRepository<AnalyticsPremiumBoughtEvent, Long> {
}
