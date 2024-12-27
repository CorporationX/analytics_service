package faang.school.analytics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEvent {
    private long id;
    private long authorId;
    private long userId;
    private LocalDateTime timestamp;

}
