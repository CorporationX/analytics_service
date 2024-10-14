package faang.school.analytics.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikeEventDto(
        long postId,
        long userId,
        LocalDateTime likedTime
) {

}
