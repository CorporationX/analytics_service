package faang.school.analytics.dto.goal;

import faang.school.analytics.dto.AnalyticsEventDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class GoalCompletedEvent {
    private Long id;
    private Long completingUserId;
    private LocalDateTime date;

    public AnalyticsEventDto toAnalyticsEventDto() {
        return AnalyticsEventDto
                .builder()
                .actorId(completingUserId)
                .receiverId(id)
                .eventType(11)
                .receivedAt(date)
                .build();
    }

}
