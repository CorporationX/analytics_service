package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedListener implements MessageListener {

    private final ObjectMapper mapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEvent event = mapper.convertValue(message.getBody(), GoalCompletedEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventRepository.save(analyticsEvent);
        log.info("Received completed goal message from channel: {}, body: {}", message.getChannel(), message.getBody());
    }
}
