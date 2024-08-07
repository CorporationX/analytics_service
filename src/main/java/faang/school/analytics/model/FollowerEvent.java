package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent {

    private long id;

    private long receiverId;

    private long actorId;

    private LocalDateTime createdAt;
}
