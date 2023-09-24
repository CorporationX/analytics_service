package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DateRange {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
}
