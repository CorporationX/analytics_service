package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalylticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventMapperTest {

    private AnalyticsEventMapperImpl analyticsEventMapper;
    private AnalylticsEventDto analylticsEventDto;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setUp() {
        analyticsEventMapper = new AnalyticsEventMapperImpl();
        analylticsEventDto = new AnalylticsEventDto();
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    public void test_toDto() {
        Assertions.assertEquals(analylticsEventDto, analyticsEventMapper.toDto(analyticsEvent));
    }
}
