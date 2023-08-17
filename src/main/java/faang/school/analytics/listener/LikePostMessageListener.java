package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.mapper.LikeEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikePostMessageListener implements MessageListener {

    private final LikeEventMapper likeEventMapper;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
            AnalyticsEvent analyticsEvent = likeEventMapper.toModel(likeEventDto);
            analyticsEvent.setEventType(EventType.POST_LIKE);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (IOException e) {
            log.error("IOException",e);
        }
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.debug("Received message from channel " + channel + ": " + body);
    }
}
