package faang.school.analytics.listener;

import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.service.PremiumEventService;
import org.springframework.stereotype.Component;

@Component
public class PremiumEventListener extends AbstractEventListener<PremiumEvent> {
    private final PremiumEventService premiumEventService;

    public PremiumEventListener(PremiumEventService premiumEventService) {
        super(PremiumEvent.class);
        this.premiumEventService = premiumEventService;
    }

    @Override
    protected void processEvent(PremiumEvent event) {
        premiumEventService.save(event);
    }
}
