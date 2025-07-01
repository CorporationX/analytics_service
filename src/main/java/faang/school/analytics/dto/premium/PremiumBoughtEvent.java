package faang.school.analytics.dto.premium;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PremiumBoughtEvent(Long userId, BigDecimal price, Currency currency, PremiumPeriod premiumPeriod,
                                 LocalDateTime startDate) {
}