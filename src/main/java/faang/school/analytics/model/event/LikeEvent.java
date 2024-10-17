package faang.school.analytics.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikeEvent(
        long postId,
        long userId,
        long likeAuthorId,
        long postAuthorId,
        LocalDateTime likedTime
) {

}
