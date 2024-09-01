package faang.school.analytics.redisListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dtoForRedis.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends EventListener<FollowerEventDto> {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, FollowerEventDto.class);
    }

    @Override
    public AnalyticsEvent toAnalyticEvent(FollowerEventDto followerEventDto) {
        return analyticsEventMapper.toAnalyticEvent(followerEventDto);
    }
}
