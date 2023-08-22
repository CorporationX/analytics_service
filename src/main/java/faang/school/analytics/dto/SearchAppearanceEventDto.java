package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAppearanceEventDto {
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;


}
