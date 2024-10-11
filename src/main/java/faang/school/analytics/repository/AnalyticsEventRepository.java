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

    @Query("""
            SELECT e FROM AnalyticsEvent e
            WHERE e.receiverId = :receiverId
            AND e.eventType = :eventType
            AND e.receivedAt BETWEEN :start AND :end
            """)
    List<AnalyticsEvent> findEventsBetweenTimes(long receiverId, EventType eventType, LocalDateTime start, LocalDateTime end);
}
