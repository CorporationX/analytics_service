package faang.school.analytics.service.analytic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AverageValueOfActionCalculator {

    public Double calculate(int totalActionsSum, int totalActionCount) {
        if (totalActionsSum == 0 || totalActionCount == 0) {
            return null;
        }
        return (double) totalActionsSum / totalActionCount;
    }
}
