package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProfileViewEvent(
        Long userId,
        Long viewerId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime viewedAt
) {
}



