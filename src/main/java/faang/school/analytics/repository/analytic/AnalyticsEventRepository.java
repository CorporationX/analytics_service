package faang.school.analytics.repository.analytic;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query(nativeQuery = true, value = """
        SELECT * 
        FROM analytics_event a WHERE
         a.received_at >= now() - interval '3 hours'
         AND a.event_type = :eventType
    """)
    Optional<List<AnalyticsEvent>> findUserActionCountByEventTypeAndTimeLimit(@Param("eventType") String eventType);
}
