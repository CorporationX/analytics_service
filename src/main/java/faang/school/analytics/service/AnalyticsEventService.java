package faang.school.analytics.service;

import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;

/**
 * Интерфейс для взаимодействия с ивентами для аналитики данных
 *
 * @author Linempy
 * @since 20.08.2025
 */
public interface AnalyticsEventService {

    /**
     * Метод сохраняет переданный ивент в базу данных
     *
     * @param event ивент
     */
    void saveEvent(AnalyticsEvent event);

    /**
     * Метод для получения аналитики по переданному параметру {@code filterDto}
     *
     * @param filterDto параметры фильтрации
     * @return отфильтрованный список ивентов
     */
    List<AnalyticsEvent> getAnalytics(RecommendationFilterDto filterDto);

}