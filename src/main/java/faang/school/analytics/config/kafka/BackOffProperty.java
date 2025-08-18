package faang.school.analytics.config.kafka;

import faang.school.analytics.service.Interval;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

import static faang.school.analytics.service.Interval.LAST_MONTH;

public record BackOffProperty(
        @DefaultValue("1000") long initInterval,
        @DefaultValue("2") int maxRetries,
        @DefaultValue("5000") long maxInterval,
        @DefaultValue("2") long multiplier
) {}
