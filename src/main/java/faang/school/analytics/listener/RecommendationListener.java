package faang.school.analytics.listener;

import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationListener implements MessageListener {

    private final JsonMapper<RecommendationEventDto> mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationEventDto dto = mapper.toObject(message.getBody(), RecommendationEventDto.class);
        System.out.println(dto);
        log.info("Received recommendation event: {}", dto);
    }
}
