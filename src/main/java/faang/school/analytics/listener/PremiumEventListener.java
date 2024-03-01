package faang.school.analytics.listener;

import faang.school.analytics.dto.event_broker.PremiumEvent;
import faang.school.analytics.service.EventService;
import org.springframework.stereotype.Component;

@Component
public class PremiumEventListener extends AbstractEventListener<PremiumEvent> {
    private final EventService eventService;

    public PremiumEventListener(EventService eventService) {
        super(PremiumEvent.class);
        this.eventService = eventService;
    }

    @Override
    protected void processEvent(PremiumEvent event) {
        eventService.savePremium(event);
    }
}
