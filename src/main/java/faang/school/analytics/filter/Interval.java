package faang.school.analytics.filter;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Interval {

    private LocalDateTime start;
    private LocalDateTime end;

    public boolean contains(LocalDateTime dateTime) {
        return !dateTime.isBefore(start) && !dateTime.isAfter(end);
    }
}
