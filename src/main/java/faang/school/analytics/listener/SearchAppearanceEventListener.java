package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.messagebroker.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEvent> {
    public SearchAppearanceEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, SearchAppearanceEvent.class);
    }

    @Override
    public AnalyticsEvent process(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_APPEARED_IN_SEARCH);
        return analyticsEvent;
    }
}
