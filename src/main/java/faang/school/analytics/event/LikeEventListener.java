package faang.school.analytics.event;

import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        analyticsEventService.saveLikeAnalytics(message);
    }
}