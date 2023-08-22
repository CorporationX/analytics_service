package faang.school.analytics.redisListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        ProfileViewEvent profileViewEvent;

        try {
            profileViewEvent = objectMapper.readValue(messageBody, ProfileViewEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Received message from channel '" + channel + "': " + messageBody);
        log.info(analyticsService.profileViewEventSave(profileViewEvent).toString());
    }
}
