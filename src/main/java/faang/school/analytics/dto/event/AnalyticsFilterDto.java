package faang.school.analytics.dto.event;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AnalyticsFilterDto {
    private Long receiverId;
    private EventType eventType;
    private Interval interval;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime from;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime to;
    private SortField sortField;
    private Sort.Direction direction;
    private Integer page;
    private Integer size;

    public AnalyticsFilterDto() {
        this.sortField = SortField.RECEIVED_AT;
        this.direction = Sort.Direction.ASC;
        this.page = 0;
        this.size = 10;
    }

    public LocalDateTime getFrom() {
        return (interval != null) ? interval.getStartTime() : from;
    }

    public LocalDateTime getTo() {
        return (interval != null) ? interval.getEndTime() : to;
    }
}
