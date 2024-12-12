package faang.school.analytics.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import faang.school.analytics.dto.EventTypeDto;
import faang.school.analytics.dto.GetAnalyticsRqDto;
import faang.school.analytics.dto.IntervalDto;
import faang.school.analytics.exception.DataValidationException;
import java.time.Month;
import org.junit.jupiter.api.Test;

class GetAnalyticsRqDtoMapperTest {

    @Test
    public void testMapperWithInterval() {
        long RECEIVER_ID = 1L;
        String EVENT_TYPE = "follower";
        String INTERVAL = "day";
        int COUNT = 2;

        GetAnalyticsRqDto result = GetAnalyticsRqMapper.map(RECEIVER_ID, EVENT_TYPE, INTERVAL, COUNT, null, null);

        assertThat(result.eventTypeDto()).isEqualTo(EventTypeDto.FOLLOWER);
        assertThat(result.intervalDto()).isEqualTo(IntervalDto.DAY);
    }

    @Test
    public void testMapperWithFromTo() {
        long RECEIVER_ID = 1L;
        String EVENT_TYPE = "follower";
        String FROM = "12.03.2024";
        String TO = "07.10.2024";

        GetAnalyticsRqDto result = GetAnalyticsRqMapper.map(RECEIVER_ID, EVENT_TYPE, null, null, FROM, TO);

        assertThat(result.from().getMonth()).isEqualTo(Month.MARCH);
        assertThat(result.to().getMonth()).isEqualTo(Month.OCTOBER);
    }

    @Test
    public void testBadIntervalValue() {
        long RECEIVER_ID = 1L;
        String EVENT_TYPE = "follower";
        String INTERVAL = "days";

        assertThrows(DataValidationException.class,
                () -> GetAnalyticsRqMapper.map(RECEIVER_ID, EVENT_TYPE, INTERVAL, null, null, null));
    }

    @Test
    public void testBadEventTypeValue() {
        long RECEIVER_ID = 1L;
        String EVENT_TYPE = "follow";

        assertThrows(DataValidationException.class,
                () -> GetAnalyticsRqMapper.map(RECEIVER_ID, EVENT_TYPE, null, null, null, null));
    }

}