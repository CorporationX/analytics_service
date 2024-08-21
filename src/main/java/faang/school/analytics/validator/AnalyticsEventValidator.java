package faang.school.analytics.validator;

import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.model.Interval;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Component
public class AnalyticsEventValidator {


    public void validate(AnalyticInfoDto analyticInfoDto) {

        LocalDateTime from = analyticInfoDto.getFrom();
        LocalDateTime to = analyticInfoDto.getTo();
        Interval interval = analyticInfoDto.getInterval();

        if (interval == null) {
            Assert.isTrue(from != null && to != null, "interval and from/to cannot be null when interval is null");
        }
    }
}
