package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.event.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsService;
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
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsService analyticsService;

    public void onMessage(Message message, byte[] pattern) {
        try {
            PostViewEventDto dto = objectMapper.readValue(message.getBody(), PostViewEventDto.class);
            AnalyticsEventDto event = analyticsEventMapper.toDto(dto);
            analyticsService.saveEvent(event);
        } catch (Exception e) {
            log.error("Failed to process post view event", e);
        }
    }
}
