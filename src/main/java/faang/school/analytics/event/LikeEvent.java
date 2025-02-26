package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {

    private Long postId;
    private Long authorId;
    private Long userId;
    private LocalDateTime likeTime;
}
