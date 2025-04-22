package faang.school.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing an analytics event")
public class AnalyticsEventDto {

    @Schema(description = "Unique event identifier", example = "12345")
    private long id;

    @Schema(description = "Event recipient ID", example = "1001")
    private long receiverId;

    @Schema(description = "Event initiator ID", example = "2002")
    private long actorId;

    @Schema(description = "Event type", example = "POST_LIKE")
    private String eventType;

    @Schema(description = "Event reception timestamp", example = "2025-04-18T12:00:00")
    private LocalDateTime receivedAt;
}
