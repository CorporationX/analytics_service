package faang.school.analytics.service.user.job;

import faang.school.analytics.service.user.listener.RedisProfileViewEventSubscriber;
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
class UserViewEventSaveJobTest {
    @Mock
    private RedisProfileViewEventSubscriber redisProfileViewEventSubscriber;

    @InjectMocks
    private UserViewEventSaveJob userViewEventSaveJob;

    @Test
    @DisplayName("Given true when check analytics events then not execute saveAllUserViewEvents")
    void testExecuteListIsEmptyTrue() {
        when(redisProfileViewEventSubscriber.analyticsEventsListIsEmpty()).thenReturn(true);
        userViewEventSaveJob.execute(mock(JobExecutionContext.class));

        verify(redisProfileViewEventSubscriber, never()).saveAllEvents();
    }

    @Test
    @DisplayName("Given false when check analytics events then execute saveAllUserViewEvents")
    void testExecuteListIsEmptyFalse() {
        when(redisProfileViewEventSubscriber.analyticsEventsListIsEmpty()).thenReturn(false);
        userViewEventSaveJob.execute(mock(JobExecutionContext.class));

        verify(redisProfileViewEventSubscriber).saveAllEvents();
    }

}