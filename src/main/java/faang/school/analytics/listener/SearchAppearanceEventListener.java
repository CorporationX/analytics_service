package faang.school.analytics.listener;


import faang.school.analytics.dto.messagebroker.SearchAppearanceEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEvent>  {

    public SearchAppearanceEventListener() {
        super(SearchAppearanceEvent.class);
    }

    @Override
    public void workingEvent(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent= analyticsEventMapper.toAnalyticEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        analyticsEventService.saveEvent(analyticsEvent);
    }


}
