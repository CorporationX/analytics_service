package faang.school.analytics.dto.user.premium;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PremiumBoughtEventDto {
    private long userId;
    private double cost;
    private int days;
    private final EventType eventType = EventType.USER_PREMIUM_BOUGHT;
    private LocalDateTime purchaseDate;
}
