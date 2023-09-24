package faang.school.analytics.messaging;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.comment.CommentEventWorker;
import faang.school.analytics.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {
    private final JsonMapper jsonMapper;
    private final CommentEventWorker commentEventWorker;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObject(message.toString(), CommentEventDto.class)
                .ifPresent(commentEventDto -> commentEventWorker.saveFollowEvent(commentEventDto));
        log.info(message+ " " + "has been handled");
    }
}
