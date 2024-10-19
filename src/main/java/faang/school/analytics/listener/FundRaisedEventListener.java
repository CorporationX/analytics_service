package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.FundRaisedEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class FundRaisedEventListener extends AbstractRedisListener<FundRaisedEvent> {
    private final AnalyticsEventMapper mapper;

    public FundRaisedEventListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper,
                                   AnalyticsEventServiceImpl analyticsEventServiceImpl) {
        super(objectMapper, analyticsEventServiceImpl);
        this.mapper = mapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message.toString());
        handleEvent(FundRaisedEvent.class, message, mapper::fromFundRaisedToEntity);
    }
}
