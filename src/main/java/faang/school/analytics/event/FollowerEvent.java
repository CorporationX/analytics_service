package faang.school.analytics.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

    @Data
    @Builder
    @Component
    @RequiredArgsConstructor
    public class FollowerEvent {
        @NotNull
        private final Long followerId;
        @NotNull
        private final Long followeeId;
        private final Long projectId;
        private final LocalDateTime subscriptionTime;
    }