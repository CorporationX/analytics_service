package faang.school.analytics.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.validation.annotation.Validated;

@Validated
public record GetAnalyticsRqDto(
        @NotNull(message = "receiverId отсутствует")
        @Min(0)
        long receiverId,
        @NotNull(message = "Параметр \"eventTypeDto\" не может быть пустым")
        EventTypeDto eventTypeDto,
        @Nullable
        IntervalDto intervalDto,
        @Nullable
        @Min(0)
        Integer count,
        @Nullable
        LocalDateTime from,
        @Nullable
        LocalDateTime to
) {

}
