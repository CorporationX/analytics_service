package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {
    private final AnalyticsEventMapper analyticsEventMapper;

    public LikeEventListener(ObjectMapper objectMapper,
                             MessageSource messageSource,
                             String channelName,
                             AnalyticsEventService service,
                             AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, messageSource, service, channelName);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message: {}", message.getBody());
        processEvent(message, LikeEvent.class, analyticsEventMapper::toEntity);
    }
}
