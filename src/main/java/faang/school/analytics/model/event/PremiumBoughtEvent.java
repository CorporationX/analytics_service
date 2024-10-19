package faang.school.analytics.model.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PremiumBoughtEvent(
        long userId,
        BigDecimal sum,
        long duration,
        LocalDateTime observeTime
) {
}
