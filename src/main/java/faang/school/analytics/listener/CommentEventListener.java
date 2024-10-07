package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CommentEventListener extends AbstractListener<CommentEvent> {

    public CommentEventListener(ObjectMapper objectMapper,
                                AnalyticsEventMapper analyticsEventMapper,
                                AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected CommentEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), CommentEvent.class);
    }

    @Override
    protected AnalyticsEvent mapToAnalyticsEvent(CommentEvent event) {
        return analyticsEventMapper.entityToAnalyticsEvent(event);
    }

}