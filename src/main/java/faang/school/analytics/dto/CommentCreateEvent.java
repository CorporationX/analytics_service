package faang.school.analytics.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CommentCreateEvent(@Positive Long postId,
                                 @Positive Long authorId,
                                 @Positive Long commentId,
                                 LocalDateTime time) {
}
