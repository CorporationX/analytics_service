package faang.school.analytics.config.scheduler.user.premium;

import faang.school.analytics.service.user.premium.job.PremiumBoughtEventSaveJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PremiumBoughtEventSaveQuartzConfig {
    private static final String NAME = "premiumBoughtEventSaveJob";
    private static final String GROUP = "premium";

    @Value("${app.quartz-config.premium-bought-event-save.trigger_interval_sec}")
    private int triggerInterval;

    @Bean
    public JobDetail premiumBoughtEventSaveJobDetail() {
        return JobBuilder.newJob(PremiumBoughtEventSaveJob.class)
                .withIdentity(NAME, GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger premiumBoughtEventSaveJobTrigger() {
        SimpleScheduleBuilder scheduler = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(triggerInterval)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(premiumBoughtEventSaveJobDetail())
                .withIdentity(NAME, GROUP)
                .withSchedule(scheduler)
                .build();
    }
}
