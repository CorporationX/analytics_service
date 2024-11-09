package faang.school.analytics.repository;

import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    AnalyticsEvent findFirstByOrderByIdDesc();

    @Modifying
    @Query("SELECT * FROM analytics_event WHERE actor_id = :id AND event_type = :eventType AND received_at >= :start AND received_at <= :end")
    List<AnalyticsEvent> getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween
            (@Param("id") long receiverId, @Param("eventType") EventType eventType,
             @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
