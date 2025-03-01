package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import faang.school.analytics.model.EventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventRequestDTO(
        long receiverId,
        EventType eventType,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime from,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime to
) {
}
