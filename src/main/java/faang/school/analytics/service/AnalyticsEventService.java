package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для управления аналитическими событиями.
 * Предоставляет операции для сохранения событий и получения аналитики.
 */
public interface AnalyticsEventService {

    /**
     * Сохраняет аналитическое событие в базу данных.
     *
     * @param event объект аналитического события для сохранения
     * @throws AnalyticsValidationException если данные события не прошли валидацию
     */
    void saveEvent(AnalyticsEvent event);

    /**
     * Сохраняет аналитическое событие в базу данных из DTO объекта.
     * Автоматически определяет тип события и преобразует в сущность через маппер.
     *
     * @param eventDto DTO объект события для сохранения (EventDto, CommentEventDto, etc.)
     * @throws AnalyticsValidationException если данные события не прошли валидацию
     * @throws IllegalArgumentException если передан неподдерживаемый тип DTO
     */
    void saveEvent(Object eventDto);

    /**
     * Получает аналитику по событиям для указанного пользователя и типа события.
     * Поддерживает фильтрацию по временному интервалу или конкретному периоду дат.
     * Результат сортируется по дате получения от поздних к ранним.
     *
     * @param receiverId идентификатор пользователя, для которого запрашивается аналитика
     * @param eventType  тип события для фильтрации
     * @param interval   временной интервал (DAY, WEEK, MONTH) - опционально
     * @param from       начальная дата периода - опционально
     * @param to         конечная дата периода - опционально
     * @return список DTO с аналитическими событиями, отсортированный по убыванию даты
     * @throws IllegalArgumentException если не указан ни interval, ни from/to
     */
    List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to);
}
