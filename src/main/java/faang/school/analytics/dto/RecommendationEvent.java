package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationEvent {
    private long recommendationId;
    private long authorId;
    private long receiverId;
    private LocalDateTime createdAt;
}
