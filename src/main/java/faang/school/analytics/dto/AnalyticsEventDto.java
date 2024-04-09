package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotBlank;
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
    @NotNull
    @Positive(message = "id must be greater than zero")
    private long receiverId;
    @NotNull
    @Positive(message = "id must be greater than zero")
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}
