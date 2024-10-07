package faang.school.analytics.dto;

import faang.school.analytics.model.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PremiumBoughtEvent {

    @NotNull
    Long userId;
    @NotNull
    BigDecimal amount;
    @NotNull
    Currency currency;
    @NotNull
    int days;
    @NotNull
    LocalDateTime timestamp;

}