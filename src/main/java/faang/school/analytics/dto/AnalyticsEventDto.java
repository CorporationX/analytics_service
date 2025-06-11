package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {
    private long id;

    private long receiverId;

    private long actorId;

    private String eventType;

    private LocalDateTime receivedAt;
}
