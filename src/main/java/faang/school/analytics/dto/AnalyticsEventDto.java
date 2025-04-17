package faang.school.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для представления события аналитики")
public class AnalyticsEventDto {

    @Schema(description = "Уникальный идентификатор события", example = "12345")
    private long id;

    @Schema(description = "ID получателя события", example = "1001")
    private long receiverId;

    @Schema(description = "ID инициатора события", example = "2002")
    private long actorId;

    @Schema(description = "Тип события", example = "POST_LIKE")
    private String eventType;

    @Schema(description = "Дата и время получения события", example = "2025-04-18T12:00:00")
    private LocalDateTime receivedAt;
}
