package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interval {
    private LocalDateTime start;
    private LocalDateTime end;

    public boolean contains(LocalDateTime time) {
        if (time == null|| start == null || end == null) {
            return false;
        }
        return (time.isAfter(start) || time.isEqual(start))
                && (time.isBefore(end) || time.isEqual(end));
    }
}
