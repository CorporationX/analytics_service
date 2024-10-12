package faang.school.analytics.service.user.premium.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.premium.PremiumBoughtEventDto;
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
public class RedisPremiumBoughtEventSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    private final List<AnalyticsEvent> analyticsEvents = new CopyOnWriteArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message: {}", message);
        try {
            List<PremiumBoughtEventDto> dtos = objectMapper.readValue(message.getBody(), new TypeReference<>() {});
            List<AnalyticsEvent> analyticsEventsNew = analyticsEventMapper.toAnalyticsEvents(dtos);
            analyticsEvents.addAll(analyticsEventsNew);
        } catch (IOException e) {
            log.error("Object mapper unread PremiumBoughtEventDto message error:", e);
        }
    }

    public boolean premiumBoughtEventListIsEmpty() {
        return analyticsEvents.isEmpty();
    }

    public void saveAllPremiumBoughtEvents() {
        List<AnalyticsEvent> analyticsEventsCopy = new ArrayList<>();
        try {
            synchronized (analyticsEvents) {
                log.info("Save premium bought event, size: {}", analyticsEvents.size());
                analyticsEventsCopy = new ArrayList<>(analyticsEvents);
                analyticsEvents.clear();
            }
            analyticsEventService.saveAllEvents(analyticsEventsCopy);
        } catch (Exception e) {
            synchronized (analyticsEvents) {
                log.error("Premium bought events save failed");
                log.warn("Save back to main list premium bought event copy, size: {}", analyticsEventsCopy);
                analyticsEvents.addAll(analyticsEventsCopy);
            }
        }
    }
}
