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

    public SearchAppearanceEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected TypeReference<SearchAppearanceEventDto> getTypeReference() {
        return new TypeReference<SearchAppearanceEventDto>() {};
    }

    @Override
    protected void handleEvent(SearchAppearanceEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        analyticsEventService.save(analyticsEvent);
    }
}
