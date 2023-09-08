package faang.school.analytics.dto.fundRasing;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FundRaisedEvent {
    private long donorId;
    private long projectId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
