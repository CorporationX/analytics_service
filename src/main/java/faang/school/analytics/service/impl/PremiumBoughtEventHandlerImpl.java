package faang.school.analytics.service.impl;

import faang.school.analytics.model.dto.PremiumBoughtEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.PremiumBoughtEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PremiumBoughtEventHandlerImpl implements PremiumBoughtEventHandler {
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void handlePremiumBoughtEvent(PremiumBoughtEventDto event) {
        analyticsEventService.handlePremiumBoughtEvent(event);
    }
}