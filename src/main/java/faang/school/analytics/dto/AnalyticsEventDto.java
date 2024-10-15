package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.event.type.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AnalyticsEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivedAt;
}
