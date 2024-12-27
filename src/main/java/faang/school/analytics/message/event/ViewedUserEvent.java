package faang.school.analytics.message.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewedUserEvent {
    private Long viewedUserId;
    private Long viewerId;
    private LocalDateTime viewedTime;
}
