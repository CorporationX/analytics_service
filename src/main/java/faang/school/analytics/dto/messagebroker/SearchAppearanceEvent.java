package faang.school.analytics.dto.messagebroker;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SearchAppearanceEvent {
    private Long userId;
    private Long searchUserId;
    private LocalDateTime dateAndTimeViewing;
}