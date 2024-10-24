package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEventDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public PostViewEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper,
            @Value("${spring.data.redis.channel.post-view}") String postViewEventChannel) {

        super(objectMapper, new ChannelTopic(postViewEventChannel));
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;

        if (postViewEventChannel == null) {
            throw new IllegalArgumentException("PostView event channel cannot be null");
        }
    }

    @Override
    public void saveEvent(PostViewEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.postViewEventDtoToAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Event saved: {}", event);
    }

    @Override
    public Class<PostViewEventDto> getEventType() {
        return PostViewEventDto.class;
    }
}
