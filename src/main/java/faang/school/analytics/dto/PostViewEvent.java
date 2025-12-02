package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PostViewEvent(
        Long postId,
        UserDto author,
        Long viewerId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime currentTime
) {
}
