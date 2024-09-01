package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.publishable.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FollowerEventListener extends AbstractListener<FollowerEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    protected void handleEvent(Message message) throws IOException {
        FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        log.info("{} has subscribed to {}", event.getFollowerId(), event.getFolloweeId());
        analyticsEventService.save(mapToAnalyticsEvent(event));
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(FollowerEvent event) {
        return analyticsEventMapper.toEntity(event);
    }

}
