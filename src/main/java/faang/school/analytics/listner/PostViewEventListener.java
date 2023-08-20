package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("PostViewEventListener has received a new message");
        try {
            PostViewEventDto eventDto = objectMapper.readValue(message.getBody(), PostViewEventDto.class);
            analyticsEventService.saveEvent(eventDto);
        } catch (IOException e) {
            log.error("IOException while parsing message in PostViewEventListener...");
            throw new RuntimeException(e);
        }
    }
}
