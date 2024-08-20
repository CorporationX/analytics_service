package faang.school.analytics.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Interval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Interval(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public static Interval parse(String intervalStr) {
        try {
            String[] parts = intervalStr.split("/");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(parts[0], formatter);
            LocalDateTime end = LocalDateTime.parse(parts[1], formatter);
            return new Interval(start, end);
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "Invalid interval format. Expected format: yyyy-MM-ddTHH:mm/yyyy-MM-ddTHH:mm");
        }
    }

    public boolean contains(LocalDateTime dateTime) {
        return (dateTime.isEqual(start) || dateTime.isAfter(start)) &&
                (dateTime.isEqual(end) || dateTime.isBefore(end));
    }
}
