package faang.school.analytics.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewEvent {
    private long id;
    private long authorId;
    private long userId;
    private LocalDateTime timestamp;

}
