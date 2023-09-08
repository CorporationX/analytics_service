package faang.school.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostViewEventDto {
    private Long viewerId;
    private Long postId;
    private LocalDateTime viewDate;
}
