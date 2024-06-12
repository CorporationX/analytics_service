package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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
public class PostViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostViewEvent event;
        try {
            log.info("Convert to dto");
            event = objectMapper.readValue(message.getBody(), PostViewEvent.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.POST_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
