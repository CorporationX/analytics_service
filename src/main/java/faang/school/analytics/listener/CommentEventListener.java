package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CommentEventListener extends AbstractListener {
    private final AnalyticsEventMapper analyticsEventMapper;

    public CommentEventListener(ObjectMapper objectMapper,
                                AnalyticsEventMapper analyticsEventMapper,
                                AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received was a message about commenting the user's post: {}", message);
        var event = readValue(message.getBody(), CommentEventDto.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.entityToAnalyticsEvent((CommentEventDto) event);
        analyticsEventService.save(analyticsEvent);
    }


}
