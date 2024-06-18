package faang.school.analytics.listener;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {
    public PostViewEventListener() {
        super(PostViewEvent.class);
    }

    @Override
    protected void saveEvent(PostViewEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.POST_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
