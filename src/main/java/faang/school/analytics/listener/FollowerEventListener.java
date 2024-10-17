package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.FollowerEvent;
import faang.school.analytics.service.FollowerEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FollowerEventListener extends AbstractRedisListener<FollowerEvent> {

    private final FollowerEventService followerEventService;
    private final ObjectMapper objectMapper;

    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService,
                                 FollowerEventService followerEventService,
                                 ObjectMapper objectMapper1) {
        super(objectMapper, analyticsEventService);
        this.followerEventService = followerEventService;
        this.objectMapper = objectMapper1;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            FollowerEvent followerEvent = objectMapper.readValue(messageBody, FollowerEvent.class);
            followerEventService.saveFollowerEvent(followerEvent);
            log.info("Processed follower event: {}", followerEvent);
        } catch (Exception e) {
            log.error("Error processing follower event: {}", message, e);
        }
    }
}