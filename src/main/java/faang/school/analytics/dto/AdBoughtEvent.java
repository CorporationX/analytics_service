package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdBoughtEvent {

    private Long postId;

    private Long userId;

    private Double paymentAmount;

    private Integer adDuration;

    private LocalDateTime eventTimestamp;
}

