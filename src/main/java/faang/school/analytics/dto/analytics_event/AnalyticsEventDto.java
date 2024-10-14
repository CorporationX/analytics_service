package faang.school.analytics.dto.analytics_event;

import faang.school.analytics.model.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "This object mustn't be null!")
@Schema(description = "Analytics Event entity returned to user")
public class AnalyticsEventDto {

    @Schema(description = "Event id", example = "1")
    @Positive
    private long id;

    @Schema(description = "User receiver id", example = "1")
    @Positive
    private long receiverId;

    @Schema(description = "Author id who did action", example = "1")
    @Positive
    private long actorId;

    @Schema(description = "Event type what was made", example = "POST_LIKE")
    private EventType eventType;

    @Schema(description = "Date and time when this event was made", example = "2024-10-08 12:26:55.152")
    private LocalDateTime receivedAt;
}
