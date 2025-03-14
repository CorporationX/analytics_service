package faang.school.analytics.dto.event;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentEventDto {
    private Long id;
    private Long authorId;
    private Long postId;
    private LocalDateTime createdAt;
}