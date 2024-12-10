package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analyticsEvent.AdBoughtEventResponseDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AdBoughtEventListener extends AbstractListener<AdBoughtEventResponseDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public AdBoughtEventListener(
            ObjectMapper objectMapper,
            List<EventHandler<AdBoughtEventResponseDto>> eventHandlers,
            AnalyticsEventService analyticsEventService,
            AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, eventHandlers);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }


    @Override
    protected AdBoughtEventResponseDto listenEvent(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), AdBoughtEventResponseDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveEvent(AdBoughtEventResponseDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.dtoToEntity(event);
        analyticsEvent.setEventType(EventType.AD_BOUGHT);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
