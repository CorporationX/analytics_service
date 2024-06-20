package faang.school.analytics.controller;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.converter.AnalyticsParametersConverter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("/analytics")
@AllArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;


    @PostMapping("/events")
    public AnalyticsEventDto save(@RequestBody AnalyticsEventDto analyticsEventDto) {
        return analyticsEventService.save(analyticsEventDto);
    }

    @GetMapping("/events/{receiverId}/{eventType}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable(name = "receiverId") @NotNull long receiverId,
                                                @PathVariable(name = "eventType") @NotNull String eventType,
                                                @RequestParam(required = false) String intervalType,
                                                @RequestParam(required = false) long intervalQuantity,
                                                @RequestParam(required = false) LocalDate from,
                                                @RequestParam(required = false) LocalDate to) {
        return analyticsEventService.getAnalytics(receiverId, EventType.valueOf(eventType), intervalType, intervalQuantity, from, to);
    }
}