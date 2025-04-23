package faang.school.analytics.dto.premium;

import faang.school.analytics.enums.PremiumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremiumAnalyticsDto {
    private PremiumType premiumType;
    private Long userId;
    private String currency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String country;
    private BigDecimal amount;
    private PremiumStatus premiumStatus;
}
