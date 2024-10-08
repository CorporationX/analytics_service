package faang.school.analytics.dto.analytics_event;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@NotNull(message = "This object cannot be null!")
@Schema(description = "This object required to get analytics of our service by /analytics GetMapping")
public class AnalyticsEventGetDto {
    @NotNull(message = "Event type cannot be null!")
    @Schema(description = "Event type what was made", example = "POST_LIKE")
    private EventType eventType;
    @Schema(description = "Interval from which you need analytics to present (can be null if from and to provided)",
            example = "3 days")
    private String interval;
    @Schema(description = "Local time and date from which needs analytics (requires to)",
            example = "2024-10-08 12:26:55.152")
    private LocalDateTime from;
    @Schema(description = "Local time and date to which needs analytics (requires from)",
            example = "2024-10-08 12:26:55.152")
    private LocalDateTime to;
}
