package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CassandraRepository<AnalyticsEvent, UUID> {
    @Query("SELECT * FROM analytics_event WHERE receiver_id = ?0 AND event_type = ?1")
    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, String eventType);
}