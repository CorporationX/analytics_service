package faang.school.analytics.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class FollowerEvent {

    @NotNull
    private final Long followerId;
    private final Long followeeId;
    private final Long projectId;
    private final LocalDateTime subscriptionTime;
}