package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FundRaisedEvent {
    private Long userId;
    private Long projectId;
    private Double raisedAmount;
    private LocalDate raiseDate;
}