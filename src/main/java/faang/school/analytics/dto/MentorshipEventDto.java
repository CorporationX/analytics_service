package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MentorshipEventDto {
    private long senderId;
    private long receiverId;
    private LocalDateTime timestamp;
}