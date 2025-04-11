package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FundRaisedEvent {
    private Long userId;
    private Long projectId;
    private BigDecimal raisedAmount;
    private LocalDate raiseDate;
}