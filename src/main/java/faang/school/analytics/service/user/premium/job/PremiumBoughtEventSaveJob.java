package faang.school.analytics.service.user.premium.job;

import faang.school.analytics.service.user.premium.listener.RedisPremiumBoughtEventSubscriber;
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
public class PremiumBoughtEventSaveJob implements Job {
    private final RedisPremiumBoughtEventSubscriber redisPremiumBoughtEventSubscriber;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!redisPremiumBoughtEventSubscriber.premiumBoughtEventListIsEmpty()) {
            log.info("Save all premium bought events job execute");
            redisPremiumBoughtEventSubscriber.saveAllPremiumBoughtEvents();
        }
    }
}
