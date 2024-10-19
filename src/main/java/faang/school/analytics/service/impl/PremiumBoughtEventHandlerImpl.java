package faang.school.analytics.service.impl;

import faang.school.analytics.model.dto.PremiumBoughtEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.PremiumBoughtEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PremiumBoughtEventHandlerImpl implements PremiumBoughtEventHandler {
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void handlePremiumBoughtEvent(PremiumBoughtEventDto event) {
        try {
            analyticsEventService.savePremiumBoughtEvent(event);
            log.info("Successfully handled PremiumBoughtEvent for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to handle PremiumBoughtEvent for user: {}", event.getUserId(), e);
        }
    }
}