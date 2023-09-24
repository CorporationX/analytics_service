package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticRequestDto {
    private long receiverId;
    private EventType eventType;
    private Interval interval;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime endDate;

    @AssertTrue(message = "Either 'interval' or both 'startDate' and 'endDate' must be provided.")
    private boolean isIntervalOrStartAndEndProvided() {
        return interval != null || (startDate != null && endDate != null);
    }
}