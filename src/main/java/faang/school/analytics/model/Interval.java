package faang.school.analytics.model;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public enum Interval {
    DAY(() -> LocalDateTime.now().minusDays(1)),
    WEEK(() -> LocalDateTime.now().minusWeeks(1)),
    MONTH(() -> LocalDateTime.now().minusMonths(1));
    
    private final Supplier<LocalDateTime> startDateSupplier;
    
    Interval(Supplier<LocalDateTime> startDateSupplier) {
        this.startDateSupplier = startDateSupplier;
    }
    
    public LocalDateTime getStartDate() {
        return startDateSupplier.get();
    }
}
