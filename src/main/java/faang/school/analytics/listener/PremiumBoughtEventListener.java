package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> implements MessageListener {
    private final AnalyticsEventMapper mapper;

    public PremiumBoughtEventListener(ObjectMapper objectMapper,
                                      MessageSource messageSource,
                                      String channelName,
                                      AnalyticsEventService service,
                                      AnalyticsEventMapper mapper) {
        super(objectMapper, messageSource, service, channelName);
        this.mapper = mapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, PremiumBoughtEvent.class, mapper::toAnalyticsEvent);
    }
}