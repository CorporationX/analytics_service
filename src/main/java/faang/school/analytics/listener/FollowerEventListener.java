package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.FollowerEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    public FollowerEventListener(AnalyticsEventService analyticsEventService, ObjectMapper objectMapper) {
        super(analyticsEventService, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, analyticsEventService::saveFollowerEvent);
    }
}
