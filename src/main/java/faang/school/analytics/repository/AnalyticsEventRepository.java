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

    @Query("SELECT e FROM AnalyticsEvent e " +
            "WHERE e.receiverId = :receiverId " +
            "AND e.eventType = :eventType " +
            "AND e.receivedAt > :from AND e.receivedAt < :to " +
            "ORDER BY e.receivedAt DESC")
    List<AnalyticsEvent> findFilteredAndSortedEvents(@Param("receiverId") long receiverId,
                                                     @Param("eventType") EventType eventType,
                                                     @Param("from") LocalDateTime from,
                                                     @Param("to") LocalDateTime to);

}
