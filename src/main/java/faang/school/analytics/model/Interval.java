package faang.school.analytics.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Interval {
    private LocalDateTime from;
    private LocalDateTime to;

    public Interval(LocalDateTime from, int intervalDays) {
        this.from = from;
        this.to = from.minusDays(intervalDays);
    }
}
