package faang.school.analytics.kafka.events;

import faang.school.analytics.kafka.Event;
import faang.school.analytics.model.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PremiumBoughtEvent extends Event {
    private EventType eventTypeEnum;
    private BigDecimal paymentAmount;
    private Integer subscriptionDuration;
    private LocalDateTime sentAt;
}
