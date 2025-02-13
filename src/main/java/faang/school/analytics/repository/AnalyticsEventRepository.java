package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query("SELECT ae FROM AnalyticsEvent ae WHERE ae.receiverId = :receiverId " +
            "AND ae.eventType = :eventType " +
            "AND ae.receivedAt >= :startTime")
    List<AnalyticsEvent> findByReceiverIdAndEventTypeAndAfterDate(
            @Param("receiverId") long receiverId,
            @Param("eventType") EventType eventType,
            @Param("startTime") LocalDateTime startTime);

    @Query("SELECT ae FROM AnalyticsEvent ae WHERE ae.receiverId = :receiverId " +
            "AND ae.eventType = :eventType " +
            "AND ae.receivedAt BETWEEN :from AND :to")
    List<AnalyticsEvent> findByReceiverIdAndEventTypeAndDateRange(
            @Param("receiverId") long receiverId,
            @Param("eventType") EventType eventType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
