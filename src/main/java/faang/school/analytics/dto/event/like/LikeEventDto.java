package faang.school.analytics.dto.event.like;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeEventDto {
    private Long postAuthorId;
    private Long likerId;
    private EventType eventType;
    private LocalDateTime createdAt;
}
