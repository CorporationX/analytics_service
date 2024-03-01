package faang.school.analytics.dto.event_broker;

import faang.school.analytics.dto.premium.PremiumPeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremiumEvent {
    private long userId;
    private long premiumId;
    private PremiumPeriod premiumPeriod;
    private LocalDateTime timestamp;
}
