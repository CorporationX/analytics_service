package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.follower.FollowerEventDto;
import faang.school.analytics.handler.EventHandler;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    public FollowerEventListener(ObjectMapper objectMapper, List<EventHandler<FollowerEventDto>> eventHandlers, AnalyticsEventService analyticsEventService) {
        super(objectMapper, eventHandlers, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, analyticsEventService::saveFollowerEvent);
    }

}
