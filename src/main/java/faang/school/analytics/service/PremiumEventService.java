package faang.school.analytics.service;

import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.dto.premium.PremiumPeriod;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static faang.school.analytics.model.EventType.*;

@Component
@RequiredArgsConstructor
public class PremiumEventService {
    private final AnalyticsEventRepository analyticsEventRepository;

    public void save(PremiumEvent premiumEvent) {
        AnalyticsEvent model = AnalyticsEvent.builder()
                .receiverId(premiumEvent.getUserId())
                .actorId(premiumEvent.getPremiumId())
                .receivedAt(premiumEvent.getTimestamp())
                .build();

        if (premiumEvent.getPremiumPeriod() == PremiumPeriod.ONE_MONTH){
            model.setEventType(PREMIUM_ONE_MONTH);
        } else if (premiumEvent.getPremiumPeriod() == PremiumPeriod.THREE_MONTHS) {
            model.setEventType(PREMIUM_THREE_MONTHS);
        } else {
            model.setEventType(PREMIUM_ONE_YEAR);
        }

        analyticsEventRepository.save(model);
    }
}
