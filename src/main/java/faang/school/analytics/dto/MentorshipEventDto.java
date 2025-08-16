package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MentorshipEventDto {
    private long senderId;
    private long receiverId;
    private LocalDateTime timestamp;
}