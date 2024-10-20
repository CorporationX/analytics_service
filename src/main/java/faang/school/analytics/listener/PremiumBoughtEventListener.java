package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.PremiumBoughtEventDto;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PremiumBoughtEventListener extends AbstractRedisListener<PremiumBoughtEventDto> {

    private final AnalyticsEventMapper mapper;

    public PremiumBoughtEventListener(ObjectMapper objectMapper, AnalyticsEventServiceImpl analyticsEventService,
                                      AnalyticsEventMapper mapper) {
        super(objectMapper, analyticsEventService);
        this.mapper = mapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            handleEvent(PremiumBoughtEventDto.class, message, mapper::fromPremiumBoughtToEntity);
        } catch (Exception e) {
            log.error("Failed to process PremiumBoughtEvent: {}", e.getMessage(), e);
        }
    }
}