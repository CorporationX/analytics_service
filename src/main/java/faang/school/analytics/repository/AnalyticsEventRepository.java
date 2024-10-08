package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);
}
