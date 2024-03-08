package faang.school.analytics.listener;


import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> {

    public PremiumBoughtEventListener() {
        super(PremiumBoughtEvent.class);
    }

    @Override
    protected AnalyticsEvent mapEvent(PremiumBoughtEvent event) {
        return eventMapper.toAnalyticsEvent(event);
    }

    @Override
    public void saveEvent(PremiumBoughtEvent event) {
        AnalyticsEvent analyticsEvent = mapEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
    }

}
