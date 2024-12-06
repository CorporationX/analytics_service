package faang.school.analytics.dto.event;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    @NotNull
    private long receiverId;
    @NotNull
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
