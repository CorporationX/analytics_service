package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundRaisedDto {
    private Long id;
    private Long projectId;
    private Long paymentAmount;
    private LocalDateTime localDateTime;
}
