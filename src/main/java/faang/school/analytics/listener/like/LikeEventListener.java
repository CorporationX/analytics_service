package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.LikeEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {
    private final LikeEventMapper mapper;
    private final AnalyticsService analyticsService;

    @Autowired
    public LikeEventListener(ObjectMapper objectMapper, LikeEventMapper mapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.mapper = mapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
            analyticsService.save(analyticsEvent);
        });
    }
}
