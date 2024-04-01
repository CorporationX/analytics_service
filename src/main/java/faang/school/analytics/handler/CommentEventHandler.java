package faang.school.analytics.handler;

import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventHandler implements EventHandler<CommentEventDto> {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    @Override
    public void handle(CommentEventDto event) {
        analyticsEventService.saveEvent(EventType.POST_COMMENT, analyticsEventMapper.toAnalyticsEvent(event));
    }

}
