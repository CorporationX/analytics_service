package faang.school.analytics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationEventDto {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime timestamp;
}