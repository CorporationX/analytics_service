package faang.school.analytics.event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationEvent {

    private long authorId;
    private long recommendationId;
    private long recipientId;
    private LocalDateTime timestamp;
}
