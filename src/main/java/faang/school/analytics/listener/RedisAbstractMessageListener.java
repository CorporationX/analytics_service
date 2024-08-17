package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
public abstract class RedisAbstractMessageListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;
}
