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

    @Query("""
            select ae from AnalyticsEvent ae
            where ae.receiverId= :id
            and ae.eventType= :type
            and ae.receivedAt between :start and :end
            """)
    List<AnalyticsEvent> findByReceiverIdAndEventTypeInInterval(@Param("id") long receiverId,
                                                                @Param("type") EventType eventType,
                                                                @Param("start") LocalDateTime start,
                                                                @Param("end") LocalDateTime end);

//    List<AnalyticsEvent> findByReceiverIdAndEventTypeAndReceivedAtBetween(long receiverId,
//                                                                           EventType eventType,
//                                                                           LocalDateTime start,
//                                                                           LocalDateTime end);
}

