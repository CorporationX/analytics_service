package faang.school.analytics.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.model.dto.PostViewEvent;
import faang.school.analytics.model.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public PostViewEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostViewEvent.class, event ->
                analyticsEventService.saveEvent(analyticsEventMapper.toEntityFromPostViewEvent(event)));
    }
}
