package faang.school.analytics.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewEvent {
    private long id;
    private long author_id;
    private long user_id;
    private LocalDateTime timeStamp;
}
