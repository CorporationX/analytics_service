package faang.school.analytics.service;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Bulgakov
 */
@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @EventListener
    public void onApplicationEvent(SearchAppearanceEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventService.save(analyticsEvent);
    }
}
