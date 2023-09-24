package faang.school.analytics.validation;

import faang.school.analytics.dto.AnalyticRequestDto;
import faang.school.analytics.exception.DataValidException;
import faang.school.analytics.model.DateRange;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;

@Component
public class AnalyticValidation {
    public AnalyticRequestDto ensureStartAndEndAreSet(AnalyticRequestDto analyticRequestDto) {
        if ((analyticRequestDto.getStartDate() == null || analyticRequestDto.getEndDate() == null) && analyticRequestDto.getInterval() == null) {
            throw new DataValidException("Interval and dates cannot be null at the same time");
        } else if (analyticRequestDto.getStartDate() == null || analyticRequestDto.getEndDate() == null) {
            DateRange range = Interval.getDateRange(analyticRequestDto.getInterval());
            analyticRequestDto.setStartDate(range.getStartDate());
            analyticRequestDto.setEndDate(range.getEndDate());
        }
        return analyticRequestDto;
    }
}