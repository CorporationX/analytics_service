package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.likeEvent.PostLikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventListener extends AbstractEventListener<PostLikeEvent> implements MessageListener {

    protected LikeEventListener(ObjectMapper objectMapper, AnalyticsEventService service, AnalyticsEventMapper mapper) {
        super(objectMapper, service, mapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostLikeEvent.class, analyticsEventMapper::toAnalyticsFromLike);
    }

}