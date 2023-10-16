package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.service.AnalyticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PostViewEventListener extends AbstractEventListener<PostViewEventDto> {


    public PostViewEventListener(ObjectMapper objectMapper, List<AnalyticService<PostViewEventDto>> analyticServices) {
        super(objectMapper, analyticServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        PostViewEventDto postViewEventDto = convertToJSON(message, PostViewEventDto.class);
        save(postViewEventDto);
        log.info("A post event received from " + channel + " :" + postViewEventDto.getPostId());
    }
}
