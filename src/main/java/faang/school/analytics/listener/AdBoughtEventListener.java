package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.dto.AdBoughtEvent;
import faang.school.analytics.model.dto.ProfileViewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class AdBoughtEventListener extends AbstractRedisListener<AdBoughtEvent> {
    private final AnalyticsEventMapper mapper;

    public AdBoughtEventListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
        this.mapper = mapper;
    }

    public void onMessage(Message message, byte[] pattern) {
        handleEvent(AdBoughtEvent.class, message, mapper::fromAdBoughtToEntity);
    }
}
