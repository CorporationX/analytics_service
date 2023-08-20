package faang.school.analytics.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MentorshipEventDto {
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime createdAt;
}
