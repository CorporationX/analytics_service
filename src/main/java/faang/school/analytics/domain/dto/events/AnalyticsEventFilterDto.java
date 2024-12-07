package faang.school.analytics.domain.dto.events;

import faang.school.analytics.model.EventType;
import faang.school.analytics.domain.enums.Interval;
import jakarta.annotation.Nullable;
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
public class AnalyticsEventFilterDto {
    @NotNull
    private Long receiverId;

    @NotNull
    private EventType eventType;

    @Nullable
    private LocalDateTime from;

    @Nullable
    private LocalDateTime to;

    @Nullable
    private Interval interval;

}
