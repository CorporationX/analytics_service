package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PostViewEventDto {

    private Long postId;
    private Long authorId;
    private Long projectId;
    private Long userId;
    private LocalDateTime viewTime;
}
