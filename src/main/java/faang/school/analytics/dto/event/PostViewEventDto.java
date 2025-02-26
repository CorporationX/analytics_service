package faang.school.analytics.dto.event;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PostViewEventDto {
    private final Long postId;
    private final Long authorId;
    private final Long userId;
    private final LocalDateTime viewedAt;
}