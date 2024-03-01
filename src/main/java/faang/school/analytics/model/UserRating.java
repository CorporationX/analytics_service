package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_rating")
public class UserRating {
    @PrimaryKey
    private UUID id;

    @Column("username")
    private String username;

    @Column("user_id")
    private Long userId;

    private Integer score ;

    @Column("score_with_penalty")
    private Integer scoreWithPenalty;

    @Column("last_active_time")
    private Instant lastActiveTime;
}