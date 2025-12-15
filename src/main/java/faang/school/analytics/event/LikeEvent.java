package faang.school.analytics.event;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeEvent {

    @NotNull
    @Positive
    private Long postId;

    @NotNull
    @Positive
    private Long authorId;

    @NotNull
    @Positive
    private Long likedByUserId;

    private LocalDateTime timestamp;
}

