package faang.school.analytics.model.event;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FundRaisedEvent(
        long userId,
        long projectId,
        BigDecimal amount,
        LocalDateTime donatedAt
) {
}
