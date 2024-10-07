package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEvent {

    @NotNull
    private Long postId;
    @NotNull
    private Long authorId;
    @NotNull
    private Long commentId;
    @NotNull
    private LocalDateTime commentedAt;

}