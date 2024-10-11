package faang.school.analytics.service.user.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisProfileViewEventSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    private final List<AnalyticsEvent> analyticsEvents = new CopyOnWriteArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received message: {}", message);
            ProfileViewEventDto dto = objectMapper.readValue(message.getBody(), ProfileViewEventDto.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(dto);
            analyticsEvents.add(analyticsEvent);
        } catch (IOException e) {
            log.error("Object mapper unread ProfileViewEventDto message error:", e);
        }
    }

    public boolean analyticsEventsListIsEmpty() {
        return analyticsEvents.isEmpty();
    }

    public void saveAllUserViewEvents() {
        List<AnalyticsEvent> analyticsEventsCopy = new ArrayList<>();
        try {
            synchronized (analyticsEvents) {
                log.info("Save profile view events, size: {}", analyticsEvents.size());
                analyticsEventsCopy = new ArrayList<>(analyticsEvents);
                analyticsEvents.clear();
            }
            analyticsEventService.saveAllEvents(analyticsEventsCopy);
        } catch (Exception e) {
            synchronized (analyticsEvents) {
                log.error("Profile view events list save failed:", e);
                log.info("Save back to main list profile view events copy, size: {}", analyticsEventsCopy.size());
                analyticsEvents.addAll(analyticsEventsCopy);
            }
        }
    }
}
