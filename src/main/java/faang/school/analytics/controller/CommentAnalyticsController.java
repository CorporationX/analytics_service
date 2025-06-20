package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/comments/analytics")
@RequiredArgsConstructor
public class CommentAnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/post/{postId}")
    public List<AnalyticsEventDto> getPostCommentAnalytics(
            @PathVariable long postId,
            @RequestParam(required = false) Interval interval,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        Optional<Interval> intervalOpt = Optional.ofNullable(interval);

        if (intervalOpt.isEmpty() && (from == null || to == null)) {
            throw new IllegalArgumentException("Необходимо указать либо интервал, либо обе даты (from и to)");
        }

        return analyticsService.getAnalytics(
                postId,
                EventType.POST_COMMENT,
                intervalOpt,
                from,
                to
        );
    }
}