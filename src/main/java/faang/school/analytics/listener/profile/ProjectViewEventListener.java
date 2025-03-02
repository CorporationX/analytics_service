package faang.school.analytics.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.channel.ChannelInfo;
import faang.school.analytics.config.redis.channel.Channels;
import faang.school.analytics.event.ProjectViewProfileEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewProfileEvent>  implements ChannelInfo {

    private final Channels channels;

    public ProjectViewEventListener(AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    ObjectMapper objectMapper, Channels channels) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
        this.channels = channels;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectViewProfileEvent.class, event -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(event));
        });
    }

    @Override
    public String getChannelName() {
        return channels.getChannelProfileView();
    }
}
