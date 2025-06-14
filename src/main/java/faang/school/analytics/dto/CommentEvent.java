package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private long postId;
    private long autorId;
    private String commentId;
    private LocalDateTime timestamp;
}
