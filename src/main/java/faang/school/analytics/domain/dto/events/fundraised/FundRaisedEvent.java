package faang.school.analytics.domain.dto.events.fundraised;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundRaisedEvent {
    @NotNull
    private Long userId;
    @NotNull
    private Long projectId;
    @NotNull
    private BigDecimal amount;

    private LocalDateTime donationTime;
}
