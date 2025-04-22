package faang.school.analytics.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Универсальная дто для аналитики
 */
@Data
public class AnalyticDto {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;
}
