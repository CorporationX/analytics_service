package faang.school.analytics.kafka.events;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FundRaisedEvent {
    private Long userId;
    private Long projectId;
    private Double amount;
    private LocalDateTime createdAt;
}
