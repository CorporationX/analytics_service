package faang.school.analytics.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostViewEvent {
    private Long postId;
    private Long authorId;
    private Long userId;
    private LocalDateTime viewedAt;
}