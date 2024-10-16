package faang.school.analytics.listener.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProfileVeiwEvent {
    private Long id;
    private long receiverId;
    private long actorId;
    private LocalDateTime receivedAt;
}
