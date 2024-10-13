package faang.school.analytics.service.user.job;

import faang.school.analytics.service.user.listener.RedisProfileViewEventSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class UserViewEventSaveJob implements Job {
    private final RedisProfileViewEventSubscriber redisProfileViewEventSubscriber;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!redisProfileViewEventSubscriber.analyticsEventsListIsEmpty()) {
            log.info("Save all user view events job execute");
            redisProfileViewEventSubscriber.saveAllEvents();
        }
    }
}
