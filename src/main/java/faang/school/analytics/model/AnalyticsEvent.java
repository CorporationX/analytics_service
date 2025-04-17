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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая событие аналитики в системе.
 * Хранит информацию о событиях, их участниках и времени возникновения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "analytics_event")
public class AnalyticsEvent {

    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Идентификатор получателя события.
     */
    @Column(name = "receiver_id", nullable = false)
    private long receiverId;

    /**
     * Идентификатор инициатора события.
     */
    @Column(name = "actor_id", nullable = false)
    private long actorId;

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
}
