package faang.school.analytics.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class FollowerEvenListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventServiceImpl analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEventDto event = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(event));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
