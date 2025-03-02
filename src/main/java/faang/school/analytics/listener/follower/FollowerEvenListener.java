package faang.school.analytics.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.channel.ChannelInfo;
import faang.school.analytics.config.redis.channel.Channels;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class FollowerEvenListener extends AbstractEventListener<FollowerEventDto> implements ChannelInfo {

    private final Channels channels;

    public FollowerEvenListener(AnalyticsEventService analyticsEventService,
                                AnalyticsEventMapper analyticsEventMapper,
                                ObjectMapper objectMapper, Channels channels) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
        this.channels = channels;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, event -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(event));
        });
    }

    @Override
    public String getChannelName() {
        return channels.getChannelFollower();
    }
}

