package faang.school.analytics.repository.criteria;

import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Builder
public class AnalyticsGetCriteria {
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    private SortField sortField;
    private SortDirection sortDirection;

    @Getter
    public enum SortField {
        RECEIVED_AT("receivedAt");

        private final String field;
        SortField(String field) { this.field = field; }
    }

    public enum SortDirection {
        ASC, DESC
    }

}