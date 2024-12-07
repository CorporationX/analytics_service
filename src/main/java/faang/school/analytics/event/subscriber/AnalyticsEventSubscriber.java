package faang.school.analytics.event.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AnalyticsEventSubscriber implements MessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            AnalyticsEvent event = objectMapper.readValue(message.getBody(), AnalyticsEvent.class);
            messageList.add(event.toString());
            log.info("Received AnalyticsEvent: {}", event);
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}