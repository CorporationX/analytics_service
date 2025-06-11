package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventRequestDto {
    private long receiverId; 
    private String eventType;
    private String interval;
    private String from;
    private String to;
}
