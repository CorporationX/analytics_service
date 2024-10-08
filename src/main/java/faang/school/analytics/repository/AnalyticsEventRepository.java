package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query(nativeQuery = true, value = """
            SELECT * FROM analytics_event
            WHERE receiver_id = :receiverId
              AND event_type = :eventType
              AND received_at >= :startDate
              AND received_at <= :endDate;
            """)
    List<AnalyticsEvent> findEventsByTime(long receiverId, EventType eventType, LocalDateTime startDate, LocalDateTime endDate);
}
