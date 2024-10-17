package faang.school.analytics.model.dto;

import faang.school.analytics.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchAppearanceEvent {
    private final EventType eventType = EventType.PROFILE_APPEARED_IN_SEARCH;
    private Long receiverId;
    private Long actorId;
    private LocalDateTime receivedAt;

}
