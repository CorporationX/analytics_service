package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
@Slf4j
public class AnalyticsEventController {
    private final AnalyticsEventService service;

    @GetMapping("/{id}/type/{type}")
    public List<AnalyticsEventDto> getAnalytics(@PathVariable("id") Long receiverId,
                                                @PathVariable("type") int type,
                                                @RequestParam String start,
                                                @RequestParam String end) {
        log.info("id {} type {} start {} end {}", receiverId, type, start, end);
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
        log.info("start {} end {}", startDate, endDate);

        AnalyticsFilterDto filterDto = AnalyticsFilterDto.builder()
                .receiverId(receiverId)
                .type(EventType.of(type))
                .startDate(startDate)
                .endDate(endDate)
                .build();
        return service.getAnalytics(filterDto);
    }
}
