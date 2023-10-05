package faang.school.analytics.messaging;

import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeEventListener implements MessageListener {

    private final JsonMapper jsonMapper;
    private final AnalyticsEventService analyticsEventService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObject(message.toString(), LikeEventDto.class)
                .ifPresent(s -> analyticsEventService.likeEventSave(s));
        log.info(message+ " " + "send");
    }
}
