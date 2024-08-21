package faang.school.analytics.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
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
public class AnalyticInfoDto {

    private long receiverId;
    private EventType eventType;
    private Interval interval;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime from;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime to;
}
