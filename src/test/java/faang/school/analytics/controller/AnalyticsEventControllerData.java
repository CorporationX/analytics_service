package faang.school.analytics.controller;

import faang.school.analytics.model.TimeIntervalType;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class AnalyticsEventControllerData {

    static Stream<Arguments> invalidFilterDto() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2025, 8, 22, 1, 1);
        LocalDateTime invalidEnd = LocalDateTime.of(2023, 1, 1, 1, 1);

        return Stream.of(
                Arguments.of(
                        TimeIntervalType.DAY,
                        start,
                        null,
                        "Нельзя указывать одновременно timeType и startTime/endTime"),
                Arguments.of(TimeIntervalType.WEEK,
                        null,
                        end,
                        "Нельзя указывать одновременно timeType и startTime/endTime"),
                Arguments.of(null, start, invalidEnd, "Дата начала не может быть позже даты окончания")
        );
    }

    static Stream<Arguments> validFilterDto() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 1, 1);
        LocalDateTime end = LocalDateTime.of(2025, 8, 22, 1, 1);

        return Stream.of(
                Arguments.of(null, start, end),
                Arguments.of(null, start, null),
                Arguments.of(null, null, end),
                Arguments.of(TimeIntervalType.WEEK, null, null),
                Arguments.of(TimeIntervalType.MONTH, null, null),
                Arguments.of(TimeIntervalType.YEAR, null, null)
        );
    }
}