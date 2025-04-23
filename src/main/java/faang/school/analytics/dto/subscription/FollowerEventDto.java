package faang.school.analytics.dto.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для события подписки пользователя.
 * <p>
 * Содержит информацию о подписчике, целевом пользователе и времени события.
 * </p>
 */
@Data
public class FollowerEventDto {
    private Long followerId;
    private Long followeeId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime timestamp;
}