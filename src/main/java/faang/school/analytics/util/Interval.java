package faang.school.analytics.util;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record Interval(
        LocalDateTime start,
        LocalDateTime end
) {
    public Interval(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public boolean contains(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }
}
