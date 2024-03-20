package faang.school.analytics.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.SearchAppearanceEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Bulgakov
 */
@Slf4j
@Component
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEventDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public SearchAppearanceEventListener(Class<SearchAppearanceEventDto> type, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(type);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }


    @Override
    public void saveEvent(SearchAppearanceEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
