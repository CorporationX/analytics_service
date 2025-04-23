package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundRaisedEvent {
    private Long userId;
    private Long projectId;
    private BigDecimal raisedAmount;
    private LocalDate raiseDate;
}