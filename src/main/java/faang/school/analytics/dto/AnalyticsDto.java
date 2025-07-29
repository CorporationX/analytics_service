package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDto {
    @NotNull
    Long postId;
    @NotNull
    Long receiverId;
    @NotNull
    Long actorId;
    @NotNull
    EventType eventType;

    LocalDateTime receivedAt;
}
