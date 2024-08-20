package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query("SELECT ae FROM AnalyticsEvent ae WHERE ae.receivedAt >= :from AND ae.receivedAt <= :to")
    List<AnalyticsEvent> getBetweenDate(LocalDateTime from, LocalDateTime to);

    @Query("SELECT ae FROM AnalyticsEvent ae WHERE ae.receivedAt >= :from")
    List<AnalyticsEvent> getByDays(LocalDateTime from);
}
