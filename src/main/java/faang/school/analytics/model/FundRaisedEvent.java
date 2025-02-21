package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundRaisedEvent {
    private Long userId;
    private Long projectId;
    private BigDecimal amount;
    private LocalDateTime donationTime;
}
