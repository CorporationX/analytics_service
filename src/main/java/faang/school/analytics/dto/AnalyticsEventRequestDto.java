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
    private String from; // e.g., "2025-06-10T14:30:00"
    private String to;   // e.g., "2025-06-10T14:30:00"
}
