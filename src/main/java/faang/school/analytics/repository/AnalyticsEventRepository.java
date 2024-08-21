package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;



@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long>, JpaSpecificationExecutor<AnalyticsEvent> {
}
