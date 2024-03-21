package faang.school.analytics.listener;

import faang.school.analytics.dto.event.SearchAppearanceEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Bulgakov
 */
@Slf4j
@Component
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEventDto> {

    public SearchAppearanceEventListener() {
        super(SearchAppearanceEventDto.class);
    }

    @Override
    public void saveEvent(SearchAppearanceEventDto event) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.PROFILE_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
