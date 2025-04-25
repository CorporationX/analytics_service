package faang.school.analytics.service;

import faang.school.analytics.dto.premium.PremiumAnalyticsDto;
import faang.school.analytics.dto.premium.PremiumType;
import faang.school.analytics.enums.PremiumStatus;
import faang.school.analytics.mapper.PremiumAnalyticsMapper;
import faang.school.analytics.mapper.PremiumAnalyticsMapperImpl;
import faang.school.analytics.model.PremiumAnalytics;
import faang.school.analytics.repository.PremiumAnalyticsRepository;
import faang.school.analytics.service.kafka.listener.PremiumAnalyticsKafkaListener;
import faang.school.analytics.service.premium.PremiumAnalyticsService;
import faang.school.analytics.service.premium.PremiumAnalyticsServiceImpl;
import faang.school.analytics.utils.JsonUtils;
import org.apache.catalina.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PremiumAnalyticsKafkaListenerTest {

    @InjectMocks
    private PremiumAnalyticsKafkaListener premiumAnalyticsKafkaListener;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private PremiumAnalyticsServiceImpl premiumAnalyticsService;

    @Mock
    private PremiumAnalyticsRepository premiumAnalyticsRepository;

    @Mock
    private Acknowledgment acknowledgment;

    @Spy
    private PremiumAnalyticsMapperImpl premiumAnalyticsMapper;

    private PremiumAnalyticsDto premiumAnalytics;

    @BeforeEach
    public void setUp() {
        LocalDateTime startDate = LocalDateTime.now();
        premiumAnalytics = PremiumAnalyticsDto.builder()
                .premiumType(PremiumType.ONE_MONTH)
                .amount(BigDecimal.TEN)
                .country("China")
                .premiumStatus(PremiumStatus.PURCHASED)
                .startDate(startDate)
                .endDate(startDate.plusMonths(1))
                .userId(1L)
                .currency("USD")
                .build();
    }

    @Test
    public void testPremiumAnalyticsListener_success() {
        when(jsonUtils.deserialize(anyString(), eq(PremiumAnalyticsDto.class))).thenReturn(premiumAnalytics);

        premiumAnalyticsKafkaListener.premiumAnalyticsListener("message", acknowledgment);

        verify(premiumAnalyticsService, times(1))
                .savePremiumAnalytics(premiumAnalytics);

        verify(acknowledgment, times(1)).acknowledge();
    }
}
