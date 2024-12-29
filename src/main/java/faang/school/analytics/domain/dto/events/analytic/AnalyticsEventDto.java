package faang.school.analytics.domain.dto.events.analytic;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
    @JsonProperty(defaultValue = "-1")
    private Integer eventTypeNumber;

    private LocalDateTime receivedAt;
}
