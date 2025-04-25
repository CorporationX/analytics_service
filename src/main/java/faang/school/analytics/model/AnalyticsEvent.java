package faang.school.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая событие аналитики в системе.
 * Хранит информацию о событиях, их участниках и времени возникновения.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "analytics_event")
public class AnalyticsEvent {

    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Идентификатор получателя события.
     */
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    /**
     * Идентификатор инициатора события.
     */
    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    /**
     * Тип события.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    /**
     * Дата и время получения события.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    public boolean isReceivedAtBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return receivedAt.isAfter(fromDate) && receivedAt.isBefore(toDate);
    }
}
