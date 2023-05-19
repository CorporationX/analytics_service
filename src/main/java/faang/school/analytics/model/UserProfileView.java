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
@Table(name="user_profile_analytics")
public class UserProfileView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="user_id", nullable = false)
    private long userId;

    @Column(name = "viewer_id", nullable = false)
    private long viewerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;
}
