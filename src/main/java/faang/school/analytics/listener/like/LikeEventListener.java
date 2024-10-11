package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEventDto;
import faang.school.analytics.listener.EventListener;
import faang.school.analytics.mapper.event.LikeEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener extends EventListener<LikeEventDto> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final LikeEventMapper likeEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(), LikeEventDto.class);
            //todo void (?) or AnalyticsEventDto eventDto  ||
            analyticsEventRepository.save(likeEventMapper.fromLikeEventToEntity(likeEventDto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
