package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumBoughtEventDto {
    private Long receiverId;
    private int amountPayment;
    private int daysSubscription;
    private LocalDateTime receivedAt;
}
