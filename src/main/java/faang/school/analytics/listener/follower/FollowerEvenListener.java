package faang.school.analytics.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class FollowerEvenListener extends AbstractEventListener<FollowerEventDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public FollowerEvenListener(AnalyticsEventService analyticsEventService,
                                AnalyticsEventMapper analyticsEventMapper,
                                ObjectMapper objectMapper) {
        super(objectMapper);

        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEventDto.class, event -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(event));
        });
    }
}

