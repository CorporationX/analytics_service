package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.AdBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdBoughtEventListener extends AbstractListener<AdBoughtEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AdBoughtEventListener(
            ObjectMapper objectMapper,
            List<EventHandler<AdBoughtEvent>> eventHandlers,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, eventHandlers);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected Class<AdBoughtEvent> eventType() {
        return AdBoughtEvent.class;
    }

    @Override
    protected void handleEvent(AdBoughtEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.dtoToEntity(event);
        analyticsEvent.setEventType(EventType.AD_BOUGHT);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
