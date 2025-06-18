package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.like.PostServiceEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {
    private final PostServiceEventMapper postServiceEventMapper;

    public LikeEventListener(AnalyticsEventService analyticsEventService,
                             ObjectMapper objectMapper,
                             PostServiceEventMapper postServiceEventMapper) {
        super(analyticsEventService, objectMapper);
        this.postServiceEventMapper = postServiceEventMapper;
    }

    @Override
    protected Class<LikeEvent> getEventClass() {
        return LikeEvent.class;
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(LikeEvent event) {
        return postServiceEventMapper.likeEventToAnalytics(event);
    }
}
