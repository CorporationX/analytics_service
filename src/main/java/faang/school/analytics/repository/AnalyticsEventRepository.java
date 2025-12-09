package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    @Query("SELECT e FROM AnalyticsEvent e " +
    "WHERE e.receiverId = :receiverId " +
    "AND e.eventType = :eventType " +
    "AND e.receivedAt BETWEEN :from AND :to")
        Stream<AnalyticsEvent> findEvents(
                @Param("receiverId") long receiverId,
                @Param("eventType") EventType eventType,
                @Param("from") LocalDateTime from,
                @Param("to") LocalDateTime to
                );
}
