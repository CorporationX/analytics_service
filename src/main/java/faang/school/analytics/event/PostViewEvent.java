package faang.school.analytics.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewEvent {

    private long postId;
    private long ownerId;
    private long viewerId;
    private LocalDateTime postViewTime;
}