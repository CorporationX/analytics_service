package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentEventDto {

    private long commentAuthorId;

    private long postAuthorId;

    private long postId;

    private long commentId;

    private String commentText;

    private LocalDateTime createdAt;
}
