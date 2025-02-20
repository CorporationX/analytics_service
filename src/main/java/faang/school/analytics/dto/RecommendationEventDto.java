package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecommendationEventDto {

    private long id;
    private Long receiverId;
    private Long requesterId;
    private LocalDateTime createdAt;

}