package faang.school.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="analytics_event")
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="receiver_id", nullable = false)
    private long receiverId;

    @Column(name = "actor_id", nullable = false)
    private long actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    public boolean isBetween(LocalDateTime from, LocalDateTime to) {
        return receivedAt.isAfter(from) && receivedAt.isBefore(to);
    }

    public static int compareByReceivedAt(AnalyticsEvent e1, AnalyticsEvent e2) {
        return e2.getReceivedAt().compareTo(e1.getReceivedAt());
    }

    public boolean isType(EventType eventType) {
        return this.eventType.equals(eventType);
    }
}
