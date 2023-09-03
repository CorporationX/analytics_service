package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsFilterDto {
    @NotNull
    private Long receiverId;
    @NotNull
    private EventType eventType;
    private LocalDateTime start;
    private LocalDateTime end;
}
