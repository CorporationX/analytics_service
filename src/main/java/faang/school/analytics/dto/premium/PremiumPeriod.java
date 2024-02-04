package faang.school.analytics.dto.premium;

import faang.school.analytics.exception.DataValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PremiumPeriod {
    ONE_MONTH(30, 10),
    THREE_MONTHS(90, 25),
    ONE_YEAR(365, 80);

    private final int days;
    private final int price;

    public static PremiumPeriod fromDays(int days) {
        return Arrays.stream(values())
                .filter(period -> period.days == days)
                .findFirst()
                .orElseThrow(() -> new DataValidationException("Нет подписки с таким периодом"));
    }
}
