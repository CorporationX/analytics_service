package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LikePostEventDto {

    @NotNull
    private Long authorPostId;

    @NotNull
    private Long likedUserId;

    @NotNull
    private Long postId;

    private LocalDateTime likeTime;

}
