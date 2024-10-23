package faang.school.analytics.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestMessage {
    private long requesterId;
    private long receiverId;
    private LocalDateTime createdAt;
}
