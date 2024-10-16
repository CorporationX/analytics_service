package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AdBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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
public class AdBoughtEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received AdBought event: {}", message.toString());
        try {
            AdBoughtEvent adBoughtEvent = objectMapper.readValue(message.getBody(), AdBoughtEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(adBoughtEvent);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("adbought with post id {} saved to database", adBoughtEvent.getPostId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
