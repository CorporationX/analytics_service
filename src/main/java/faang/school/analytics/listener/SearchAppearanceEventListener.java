package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class SearchAppearanceEventListener extends AbstractRedisListener<SearchAppearanceEvent> {
    private final AnalyticsEventMapper mapper;

    public SearchAppearanceEventListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper,
                                         AnalyticsEventServiceImpl analyticsEventServiceImpl) {
        super(objectMapper, analyticsEventServiceImpl);
        this.mapper = mapper;
    }

    public void onMessage(Message message, byte[] pattern) {
        handleEvent(SearchAppearanceEvent.class, message, mapper::fromSearchAppearanceToEntity);
    }
}
