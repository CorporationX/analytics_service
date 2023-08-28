package faang.school.analytics.service.comment;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentEventWorker {
    private final AnalyticsEventService analyticsEventService;

    public void saveFollowEvent(CommentEventDto commentEventDto) {
        analyticsEventService.commentEventSave(commentEventDto);
    }
}
