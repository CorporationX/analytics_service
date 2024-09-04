package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PremiumBoughtEvent {
    private Long userId;
    private Long paymentAmount;
    private Long subscriptionDuration;
    private LocalDateTime timestamp;
}