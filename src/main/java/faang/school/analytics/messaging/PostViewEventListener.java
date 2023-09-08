package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.service.AnalyticsService;
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
    private final AnalyticsService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        PostViewEventDto postViewEventDto;

        try {
            postViewEventDto = objectMapper.readValue(body, PostViewEventDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        analyticsService.savePostViewEvent(postViewEventDto);
        log.info("A post event received from " + channel + " :" + postViewEventDto.getPostId());
    }
}
