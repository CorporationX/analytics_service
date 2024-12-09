package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.likeEvent.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsFromLike(likeEvent);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Processed and saved LikeEvent: {}", likeEvent);
        } catch (Exception e) {
            log.error("Error processing LikeEvent message", e);
        }
    }
}