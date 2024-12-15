package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikePostEventDto {

    @NotNull
    private Long authorPostId;

    @NotNull
    private Long likedUserId;

    @NotNull
    private Long postId;

    private LocalDateTime likeTime;

}
