package faang.school.analytics.listener;


import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> {
    @Autowired
    private AnalyticsEventService<PremiumBoughtEvent> premiumBoughtService;

    public PremiumBoughtEventListener() {
        super(PremiumBoughtEvent.class);
    }

    @Override
    public void saveEvent(PremiumBoughtEvent event) {
        premiumBoughtService.saveEvent(event);
    }

}
