package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEvent extends BaseEvent{
    private long postId;
    private long authorId;
    private long userId;
    private LocalDateTime timeStamp;
}
