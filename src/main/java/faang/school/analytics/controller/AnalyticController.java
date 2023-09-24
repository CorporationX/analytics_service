package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticRequestDto;
import faang.school.analytics.dto.AnalyticResponseDto;
import faang.school.analytics.service.AnalyticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    public List<AnalyticResponseDto> getAnalytics(@RequestBody @Valid AnalyticRequestDto analyticRequestDto) {
        return analyticService.getAnalytics(analyticRequestDto);
    }
}