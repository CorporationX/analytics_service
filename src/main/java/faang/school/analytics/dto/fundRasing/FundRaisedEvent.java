package faang.school.analytics.dto.fundRasing;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FundRaisedEvent {
    private long donorId;
    private long projectId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
