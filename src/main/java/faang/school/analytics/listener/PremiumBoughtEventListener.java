package faang.school.analytics.listener;


import faang.school.analytics.dto.PremiumBoughtEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEventDto> {

    public PremiumBoughtEventListener() {
        super(PremiumBoughtEventDto.class);
    }


    @Override
    public void saveEvent(PremiumBoughtEventDto event) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
    }

}
