package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);
}
