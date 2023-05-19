package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.UserProfileViewEvent;
import faang.school.analytics.service.UserProfileAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProfileViewListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final UserProfileAnalyticsService userProfileAnalyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var view = objectMapper.readValue(message.getBody(), UserProfileViewEvent.class);
            log.info("Received message: {}", view);
            userProfileAnalyticsService.saveView(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
