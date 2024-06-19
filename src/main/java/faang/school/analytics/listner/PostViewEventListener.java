package faang.school.analytics.listner;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostViewEventListener extends AbstractEventListener<PostViewEvent>  {

    @Autowired
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
