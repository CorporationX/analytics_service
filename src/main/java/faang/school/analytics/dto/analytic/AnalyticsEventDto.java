package faang.school.analytics.dto.analytic;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventDto {
    private Long id;

    @NotNull
    private Long receiverId;

    @NotNull
    private Long actorId;

    @JsonProperty(defaultValue = "-1")
    private int eventTypeNumber;

    private LocalDateTime receivedAt;
}
