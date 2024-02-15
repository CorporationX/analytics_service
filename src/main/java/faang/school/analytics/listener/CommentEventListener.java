package faang.school.analytics.listener;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Autowired
    public CommentEventListener(AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(CommentEventDto.class);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected void processEvent(CommentEventDto event) {
        log.info("Start processing an incoming event - {}", event);
        analyticsEventService.saveEvent(analyticsEventMapper.toEntity(event));
    }
}