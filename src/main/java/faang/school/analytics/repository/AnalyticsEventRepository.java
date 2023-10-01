package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query(nativeQuery = true, value = """
            SELECT * FROM analytics_event where received_at >= NOW() - interval '1 day'
            """)
    List<AnalyticsEvent> getLatestEvents(String latestPeriodOfTime);

    @Query(nativeQuery = true, value = """
            SELECT * FROM analytics_event
            WHERE received_at >= :startDate
            AND receiver_id = :receiverId
            AND event_type = :eventType
            ORDER BY received_at DESC
            """)
    List<AnalyticsEvent> getEventsByInterval(@Param("startDate") LocalDateTime startDate,
                                             @Param("receiverId") long receiverId,
                                             @Param("eventType") String eventType);

    @Query(nativeQuery = true, value = """
            SELECT * FROM analytics_event
            WHERE received_at >= :fromDate
            AND received_at <= :toDate
            AND receiver_id = :receiverId
            AND event_type = :eventType
            ORDER BY received_at DESC
            """)
    List<AnalyticsEvent> getEventsByDates(@Param("fromDate") LocalDateTime fromDate,
                                          @Param("toDate") LocalDateTime toDate,
                                          @Param("receiverId") long receiverId,
                                          @Param("eventType") String eventType);
}
