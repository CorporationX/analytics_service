package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostViewEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostViewEvent event = objectMapper.convertValue(
                message.getBody(), PostViewEvent.class);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

    }
}
