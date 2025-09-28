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

    @Query(nativeQuery = true, value = """
            INSERT INTO analytics_event (actor_id, receiver_id, event_type, received_at)
            VALUES (?1, ?2, 0, NOW())
            """)
    void save(long requesterId, long receiverId, LocalDateTime received_at);


}
