package faang.school.analytics.service.analytic;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardDeviationCalculator {

    public Double calculate(List<Integer> sumOfUsersActions, Double averageValueOfAction, int activeUsersSize) {
        if(sumOfUsersActions == null || averageValueOfAction == null || activeUsersSize == 0) {
            return null;
        }
        double standardDeviation = sumOfUsersActions.stream()
                .mapToDouble(num -> Math.pow(num - averageValueOfAction, 2))
                .sum() / activeUsersSize;
        return Math.sqrt(standardDeviation);
    }
}
