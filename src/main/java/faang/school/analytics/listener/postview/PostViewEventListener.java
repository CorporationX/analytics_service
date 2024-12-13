package faang.school.analytics.listener.postview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.service.analytic.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            AnalyticsEventDto event = objectMapper.readValue(json, AnalyticsEventDto.class);
            analyticsEventService.savePostView(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
