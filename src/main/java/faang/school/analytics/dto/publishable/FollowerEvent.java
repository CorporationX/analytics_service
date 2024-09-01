package faang.school.analytics.dto.publishable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FollowerEvent {
    @NotNull
    private Long followerId;
    @NotNull
    private Long followeeId;
    @NotNull
    private LocalDateTime subscriptionTime;
}
