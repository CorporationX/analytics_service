package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticDto {
    Long id;
    EventType type;
}
