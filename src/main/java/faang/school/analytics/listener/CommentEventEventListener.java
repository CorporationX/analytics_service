package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class CommentEventEventListener extends AbstractEventListener<CommentEventDto> {


    public CommentEventEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(CommentEventDto.class, objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected void saveEvent(CommentEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toCommentEntity(event);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
