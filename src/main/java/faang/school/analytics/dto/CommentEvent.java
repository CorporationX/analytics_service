package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
