package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.EventDto;
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
public class AnalyticsMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
            String channel = new String(message.getChannel());

        try {
            EventDto eventDto = objectMapper.readValue(message.getBody(), EventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(eventDto);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("Analytics event saved: {}", eventDto);
        } catch (Exception e) {
            log.error("Failed to process analytics event from channel: {}", channel, e);
        }
    }
}