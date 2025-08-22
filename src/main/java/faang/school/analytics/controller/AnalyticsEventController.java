package faang.school.analytics.controller;

import faang.school.analytics.dto.RecommendationFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для управления ивентами аналитики
 *
 * @author Linempy
 * @since 20.08.2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsEventController {

    private final AnalyticsEventService service;

    /**
     * Метод позволяет получить отфильтрованные данные по {@code filterDto}
     *
     * @param filterDto параметры фильтрации
     * @return отфильтрованный список данных
     */
    @GetMapping
    public ResponseEntity<List<AnalyticsEvent>> getByFilter(@Valid @ModelAttribute RecommendationFilterDto filterDto) {
        filterDto.validate();
        List<AnalyticsEvent> result = service.getAnalytics(filterDto);
        return ResponseEntity.ok(result);
    }
}