package faang.school.analytics.domain.dto.events;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {
    private Long id;

    @NotNull
    private Long receiverId;

    @NotNull
    private Long actorId;

    @NotNull
    private Integer eventType;

    private LocalDateTime receivedAt;
}
