package faang.school.analytics.event.follower;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEvent {

    @NotNull
    private Long actorId;

    @NotNull
    private Long receiverId;

    private LocalDateTime receivedAt;
}
