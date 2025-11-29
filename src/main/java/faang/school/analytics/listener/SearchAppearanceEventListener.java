package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventServiceImpl analyticsEventService;

    public SearchAppearanceEventListener(ObjectMapper objectMapper,
                                         AnalyticsEventMapper analyticsEventMapper,
                                         AnalyticsEventServiceImpl analyticsEventService) {
        super(objectMapper, SearchAppearanceEvent.class);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    protected void eventConsumer(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}