package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEvent> {
    public SearchAppearanceEventListener(ObjectMapper objectMapper,
                                         AnalyticsEventRepository analyticsEventRepository,
                                         AnalyticsEventMapper analyticsEventMapper,
                                         AnalyticsEventService analyticsEventService) {
        super(objectMapper, SearchAppearanceEvent.class, analyticsEventRepository, analyticsEventMapper, analyticsEventService);
    }

    @Override
    protected void eventConsumer(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = getAnalyticsEventMapper().toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        getAnalyticsService().saveEvent(analyticsEvent);
    }
}