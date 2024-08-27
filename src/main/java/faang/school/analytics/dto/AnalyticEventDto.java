package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticEventDto {

    private Long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime receivedAt;
}
