package faang.school.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "analytics_premium_bought_event")
public class AnalyticsPremiumBoughtEvent extends AnalyticsEvent {

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "subscription_duration")
    private Integer subscriptionDurationDays;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
