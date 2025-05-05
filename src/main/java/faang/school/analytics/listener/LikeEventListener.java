package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class LikeEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    public void onMessage(LikeEvent likeEvent) {
        log.info("Получено событие liked_post_topic: {}", likeEvent);
            analyticsEventService.handleLikeEvent(likeEvent);
    }
}
