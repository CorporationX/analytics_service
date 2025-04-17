package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Сервис для работы с аналитикой событий.
 * <p>
 * Предоставляет методы для получения и обработки данных о событиях.
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    /**
     * Возвращает список событий аналитики, отфильтрованных по получателю, типу события и временному диапазону.
     *
     * @param receiverId ID получателя событий
     * @param eventType  тип события
     * @param fromDate   начальная дата диапазона
     * @param toDate     конечная дата диапазона
     * @return список {@link AnalyticsEventDto}
     */
    @Transactional(readOnly = true)
    public List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType,
                                                LocalDateTime fromDate, LocalDateTime toDate) {
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        return events.filter(event ->
                        event.getReceivedAt().isAfter(fromDate) &&
                                event.getReceivedAt().isBefore(toDate))
                .map(analyticsEventMapper::toDto)
                .toList();
    }
}