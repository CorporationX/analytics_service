package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerEvent {
    private Long receiverId;
    private Long followerId;
    private Long projectId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime followingDate;
}