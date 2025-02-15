package faang.school.analytics.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"@class"})
public class CommentEvent {
    private Long commentId;
    private String comment;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
}
