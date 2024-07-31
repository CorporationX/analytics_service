package faang.school.analytics.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private Long commentAuthorId;
    private Long postAuthorId;
    private Long postId;
    private Long commentId;
}
