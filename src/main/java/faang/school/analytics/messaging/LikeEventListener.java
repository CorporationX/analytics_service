package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import faang.school.analytics.service.redis.events.LikeEvent;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    @Autowired
    public LikeEventListener
            (ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper, AnalyticsService analyticsService) {
        super(objectMapper, analyticsEventMapper, analyticsService);
    }


    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        LikeEvent likeEvent = mapEvent(message, LikeEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.likeEventToAnalyticsEvent(likeEvent);
        analyticsService.create(analyticsEvent);
    }
}
