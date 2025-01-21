package faang.school.analytics.mapper;

import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.dto.IntervalDto;
import faang.school.analytics.exception.DataValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GetAnalyticsRqMapper {

    public static GetAnalyticsRqDto map(long receiverId, String eventType, String interval, Integer count, String from,
            String to) {
        EventTypeDto eventTypeDto;
        LocalDate dateFrom = null;
        LocalDate dateTo = null;
        IntervalDto intervalDto = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        if (from != null) {
            dateFrom = LocalDate.parse(from, formatter);
        }
        if (to != null) {
            dateTo = LocalDate.parse(to, formatter);
        }

        try {
            eventTypeDto = EventTypeDto.valueOf(eventType.toUpperCase());

            if(interval != null) {
                intervalDto = IntervalDto.of(interval);
            }
        } catch (IllegalArgumentException ex) {
            throw new DataValidationException("Некорректно введён тип: \n" + ex.getMessage());
        }

        return new GetAnalyticsRqDto(receiverId, eventTypeDto, intervalDto, count,
                dateFrom != null ? LocalDateTime.of(dateFrom, LocalTime.of(0, 0, 0)) : null,
                dateTo != null ? LocalDateTime.of(dateTo, LocalTime.of(0, 0, 0)) : null);
    }
}
