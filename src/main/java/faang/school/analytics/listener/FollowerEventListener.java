package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);

            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);

            analyticsEventService.saveEvent(analyticsEvent);

            log.debug("FollowerEvent processed and saved. followerId={}, followeeId={}, timestamp={}",
                    event.getFollowerId(), event.getFolloweeId(), event.getTimestamp());


        } catch (Exception e) {
            log.error("Failed to process FollowerEvent message: {}", new String(message.getBody()), e);
        }
    }
}

