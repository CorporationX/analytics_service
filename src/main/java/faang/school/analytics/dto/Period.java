package faang.school.analytics.dto;

import jakarta.annotation.Nullable;
import java.time.LocalDateTime;

public record Period(
        @Nullable LocalDateTime beginDate,
        @Nullable LocalDateTime endDate
) {

}
