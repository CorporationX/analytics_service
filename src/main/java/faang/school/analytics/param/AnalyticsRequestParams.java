package faang.school.analytics.param;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
public class AnalyticsRequestParams {

    @NotNull(message = "Receiver Id cannot be blank!")
    private Long receiverId;

    @NotNull(message = "Event type cannot be blank!")
    private EventType eventType;

    private Interval interval;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AnalyticsRequestParams(Long receiverId, String eventType, Optional<String> interval,
                                  Optional<String> startDate, Optional<String> endDate) {
        this.receiverId = receiverId;
        this.eventType = parseEventType(eventType);

        interval.ifPresent(value -> this.interval = parseInterval(value));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        startDate.ifPresent(value -> this.startDate = LocalDateTime.parse(value, formatter));
        endDate.ifPresent(value -> this.endDate = LocalDateTime.parse(value, formatter));

        validateDates();
        if(this.startDate != null && this.endDate != null) {
            validStartAndEndDates(this.startDate, this.endDate);
        }
    }

    private EventType parseEventType(String eventType) {
        try {
            int type = Integer.parseInt(eventType);
            return EventType.of(type);
        } catch (NumberFormatException e) {
            return EventType.valueOf(eventType.toUpperCase());
        }
    }

    private Interval parseInterval(String interval) {
        try {
            int type = Integer.parseInt(interval);
            return Interval.of(type);
        } catch (NumberFormatException e) {
            return Interval.fromString(interval);
        }
    }

    private void validateDates() {
        if (interval == null && (startDate == null || endDate == null)) {
            throw new IllegalArgumentException("Either interval or both startDate and endDate must be provided!");
        }
    }

    private void validStartAndEndDates(LocalDateTime startDate, LocalDateTime endDate) {
        if(endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than start date!");
        }
    }

}
