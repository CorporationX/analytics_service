package faang.school.analytics.handler;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.dto.event.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FollowerEventHandler implements EventHandler<FollowerEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public boolean canHandle(FollowerEvent event) {
        return true;
    }

    @Override
    public void handle(FollowerEvent event) {
        AnalyticsEventDto eventDto = analyticsEventMapper.fromFollowerEventToDto(event);
        eventDto.setEventType(EventType.FOLLOWER);
        analyticsEventService.save(eventDto);
    }
}