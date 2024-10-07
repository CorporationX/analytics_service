package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerEvent {

    @NotNull
    private long followerId;
    @NotNull
    private long followeeId;
    @NotNull
    private LocalDateTime subscriptionDateTime;

}