package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Autowired
    public CommentEventListener(ObjectMapper objectMapper,
                                AnalyticsEventService analyticsEventService,
                                AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void saveBatch(List<CommentEvent> events) {
        List<AnalyticsEvent> analyticsEvents = analyticsEventMapper.toAnalyticsEvents(events);
        analyticsEventService.saveAllEvents(analyticsEvents);
        log.info("Successfully saved batch of {} events", events.size());
    }

    @Override
    public Class<CommentEvent> getEventType() {
        return CommentEvent.class;
    }
}
