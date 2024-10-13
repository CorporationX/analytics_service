package faang.school.analytics.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class RedisEventSubscriberBatchSave<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;
    private final List<AnalyticsEvent> analyticsEvents = new CopyOnWriteArrayList<>();

    protected void addToList(Message message, Class<T> clazz,
                             Function<List<T>, List<AnalyticsEvent>> analyticEventsMapfunction) {
        try {
            List<T> dtos = objectMapper.readValue(message.getBody(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            List<AnalyticsEvent> analyticsEventsNew = analyticEventsMapfunction.apply(dtos);
            analyticsEvents.addAll(analyticsEventsNew);
        } catch (IOException e) {
            log.error("Object mapper unread message error:", e);
        }
    }

    public boolean analyticsEventsListIsEmpty() {
        return analyticsEvents.isEmpty();
    }

    public void saveAllEvents() {
        List<AnalyticsEvent> analyticsEventsCopy = new ArrayList<>();
        try {
            synchronized (analyticsEvents) {
                log.info("Save all events, size: {}", analyticsEvents.size());
                analyticsEventsCopy = new ArrayList<>(analyticsEvents);
                analyticsEvents.clear();
            }
            analyticsEventService.saveAllEvents(analyticsEventsCopy);
        } catch (Exception e) {
            synchronized (analyticsEvents) {
                log.error("Events list save failed:", e);
                log.info("Save back to main list events copy, size: {}", analyticsEventsCopy.size());
                analyticsEvents.addAll(analyticsEventsCopy);
            }
        }
    }
}
