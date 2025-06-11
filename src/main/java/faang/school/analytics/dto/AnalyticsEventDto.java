package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventDto {
    private long receiverId;
    private long actorId;
    private String eventType;
    private String receivedAt;
}
