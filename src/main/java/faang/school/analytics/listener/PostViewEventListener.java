package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final PostViewEventMapper postViewEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (message.getBody() == null) {
            log.warn("Received a null message or message body. Ignoring.");
            return;
        }
        try {
            PostViewEvent event = objectMapper.readValue(message.getBody(), PostViewEvent.class);
            log.info("Received PostViewEvent: {}", event);
            AnalyticsEventDto analyticsEventDto = postViewEventMapper.postViewToAnalyticEventDto(event);
            analyticsEventService.saveEvent(analyticsEventDto);
            log.info("Saved AnalyticsEventDto: {}", analyticsEventDto);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
