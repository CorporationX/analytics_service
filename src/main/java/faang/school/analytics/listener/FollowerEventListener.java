package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.AbstractEventDto;
import faang.school.analytics.dto.event.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {
    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(),
                    FollowerEventDto.class);
            log.info(new String(message.getBody()));
            sendAnalytics(followerEventDto);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.FOLLOWER;
    }
}
