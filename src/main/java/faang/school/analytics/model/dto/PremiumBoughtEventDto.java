package faang.school.analytics.model.dto;

import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.PremiumPeriod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PremiumBoughtEventDto {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotNull(message = "amount cannot be null")
    private BigDecimal amount;

    @NotNull(message = "subscriptionDuration cannot be null")
    private PremiumPeriod subscriptionDuration;

    private LocalDateTime receivedAt;

    private String premiumType;

    private EventType eventType = EventType.PREMIUM_BOUGHT;
}