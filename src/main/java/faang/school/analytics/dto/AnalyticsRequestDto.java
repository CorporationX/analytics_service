package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsRequestDto {
    private Long receiverId;
    private String eventType;
    private String interval;
    private String startDate;
    private String endDate;
}
