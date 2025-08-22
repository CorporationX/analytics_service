package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Класс-слушатель ивентов-рекомендаций
 *
 * @author Linempy
 * @since 20.08.2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventService service;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            AnalyticsEvent analyticsEvent = eventMapper.toEntity(event);
            log.debug("Этап маппинга рекомендации пройден");
            service.saveEvent(analyticsEvent);
        } catch (IOException e) {
            log.warn("Ошибка при десериализации ивента-рекомендации");
            throw new RuntimeException(e);
        }
    }
}