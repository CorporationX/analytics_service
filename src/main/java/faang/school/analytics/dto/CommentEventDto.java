package faang.school.analytics.dto;


import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentEventDto {
    private Long commentId;
    private Long commentAuthorId;
    private Long postId;
    private Long postAuthorId;
    private String commentContent;
    private LocalDateTime receivedAt;
    private final EventType eventType = EventType.POST_COMMENT;
}
