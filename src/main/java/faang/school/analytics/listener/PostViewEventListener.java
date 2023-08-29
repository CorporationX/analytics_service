package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.redis.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostViewEventListener extends AbstractListener<PostViewEventDto> {
    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventRepository repository) {
        super(objectMapper, analyticsEventMapper, repository);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostViewEventDto postViewEventDto = readValue(message.getBody(), PostViewEventDto.class);
        log.info("Received message: {}", postViewEventDto);
        save(analyticsEventMapper.toEntity(postViewEventDto));
    }
}
