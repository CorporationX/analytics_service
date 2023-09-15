package faang.school.analytics.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewEvent {
    private Long userId;
    private Long profileViewedId;
    private LocalDateTime date;
}
