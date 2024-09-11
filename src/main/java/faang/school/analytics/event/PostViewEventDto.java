package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostViewEventDto {
    private Long id;
    private Long authorId;
    private Long userId;
    private LocalDateTime receivedAt;
}