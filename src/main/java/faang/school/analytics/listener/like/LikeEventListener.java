package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {

//    private final AnalyticsEventService service;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
//            service.save(likeEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
