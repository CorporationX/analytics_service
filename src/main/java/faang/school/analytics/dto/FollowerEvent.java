package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class FollowerEvent {
    @NotNull
    private Long followerId;
    @NotNull
    private Long followeeId;
    @NotNull
    private LocalDateTime followTime;
}
