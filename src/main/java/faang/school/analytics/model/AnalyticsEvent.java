package faang.school.analytics.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("analytics_event")
public class AnalyticsEvent {
    @PrimaryKey
    private UUID id;

    @Column("receiver_id")
    private Long receiverId;

    @Column("actor_id")
    private Long actorId;

    @Column("event_type")
    private String eventType;

    @Column("received_at")
    private LocalDateTime receivedAt;
}
