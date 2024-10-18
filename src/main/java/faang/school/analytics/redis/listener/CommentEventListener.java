package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public CommentEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper,
            @Value("${spring.data.redis.channel.comment}") String commentEventChannel) {

        super(objectMapper, new ChannelTopic(commentEventChannel));
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;

        if (commentEventChannel == null) {
            throw new IllegalArgumentException("Comment event channel cannot be null");
        }
    }

    @Override
    public void saveEvent(CommentEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Event saved: {}", event);
    }

    @Override
    public Class<CommentEvent> getEventType() {
        return CommentEvent.class;
    }
}