package faang.school.analytics.model.interval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interval {
    private TypeOfInterval typeOfInterval;
    private long quantity;

    public LocalDateTime getStartDateTime(LocalDateTime endTime) {
        switch (typeOfInterval) {
            case MINUTES -> {
                return endTime.minusMinutes(quantity);
            }
            case HOURS -> {
                return endTime.minusHours(quantity);
            }
            case DAYS -> {
                return endTime.minusDays(quantity);
            }
            case WEEKS -> {
                return endTime.minusWeeks(quantity);
            }
            case MONTH -> {
                return endTime.minusMonths(quantity);
            }
            case YEARS -> {
                return endTime.minusYears(quantity);
            }
            default -> {
                return LocalDateTime.now();
            }
        }
    }
}