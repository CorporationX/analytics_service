package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {

    private long id;

    @Positive
    private long receiverId;

    @Positive
    private long actorId;

    @NotBlank
    private EventType eventType;

    private LocalDateTime receivedAt;
}
