package faang.school.analytics.controller;

import faang.school.analytics.controller.common.ApiExceptionDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.RequestAnalyticsDto;
import faang.school.analytics.service.AnalyticsEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsEventController {

    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    @Operation(summary = "Предоставляет аналитику")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аналитика предоставлена",
    content = {
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventDto.class)
            )
    }
    ),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = {
                            @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ApiExceptionDto.class)
                            )
                    }
            )
    })

    public ResponseEntity<List<EventDto>> getEvents(@RequestBody @Valid RequestAnalyticsDto requestAnalyticsDto) {
        return ResponseEntity.ok(analyticsEventService.getAnalitics(requestAnalyticsDto));
    }
}
