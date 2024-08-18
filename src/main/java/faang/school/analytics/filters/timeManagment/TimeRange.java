package faang.school.analytics.filters.timeManagment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class TimeRange {
    private LocalDateTime start;
    private LocalDateTime end;
    public boolean isCompletelyFilled (){
        return start != null && end != null;
    }
    public boolean isEmpty(){
        return start == null && end == null;
    }
}
