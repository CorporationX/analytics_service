package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query("""
            FROM AnalyticsEvent ae
            WHERE ae.receiverId = :receiverId
                AND ae.eventType = :eventType
                AND ae.receivedAt > :from
                AND ae.receivedAt < :to
            ORDER BY ae.receivedAt DESC
            """)
    Stream<AnalyticsEvent> findByReceiverIdAndEventTypeThenFilterByDateAndSortByTimeDesc(long receiverId,
                                                                                         EventType eventType,
                                                                                         LocalDateTime from,
                                                                                         LocalDateTime to);
}
