package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class CommentEventEventListener extends AbstractEventListener<CommentEventDto> {

    public CommentEventEventListener() {
        super(CommentEventDto.class);
    }

    @Override
    protected void saveEvent(CommentEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toCommentEntity(event);
        analyticsEvent.setEventType(EventType.POST_COMMENT);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
