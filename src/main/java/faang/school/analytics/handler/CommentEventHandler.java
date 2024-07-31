package faang.school.analytics.handler;

import faang.school.analytics.dto.comment.CommentEvent;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentEventHandler implements EventHandler<CommentEvent>{
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;


    @Override
    public boolean canHandle(CommentEvent event) {
        return event != null;
    }

    @Override
    public void handle(CommentEvent event) {
        AnalyticsEventDto dto = analyticsEventMapper.fromCommentEventToDto(event);
        analyticsEventService.save(dto);
    }
}
