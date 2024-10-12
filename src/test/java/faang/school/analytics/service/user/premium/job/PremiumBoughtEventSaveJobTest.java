package faang.school.analytics.service.user.premium.job;

import faang.school.analytics.service.user.premium.listener.RedisPremiumBoughtEventSubscriber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumBoughtEventSaveJobTest {
    @Mock
    private RedisPremiumBoughtEventSubscriber redisPremiumBoughtEventSubscriber;

    @InjectMocks
    private PremiumBoughtEventSaveJob premiumBoughtEventSaveJob;

    @Test
    @DisplayName("Given true when check analytics events then not execute saveAllPremiumBoughtEvents")
    void testExecuteListIsEmptyTrue() {
        when(redisPremiumBoughtEventSubscriber.premiumBoughtEventListIsEmpty()).thenReturn(true);
        premiumBoughtEventSaveJob.execute(mock(JobExecutionContext.class));

        verify(redisPremiumBoughtEventSubscriber, never()).saveAllPremiumBoughtEvents();
    }

    @Test
    @DisplayName("Given false when check analytics events then execute saveAllPremiumBoughtEvents")
    void testExecuteListIsEmptyFalse() {
        when(redisPremiumBoughtEventSubscriber.premiumBoughtEventListIsEmpty()).thenReturn(false);
        premiumBoughtEventSaveJob.execute(mock(JobExecutionContext.class));

        verify(redisPremiumBoughtEventSubscriber).saveAllPremiumBoughtEvents();
    }
}