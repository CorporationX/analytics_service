package faang.school.analytics.config.scheduler.user;

import faang.school.analytics.service.user.job.UserViewEventSaveJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserViewEventSaveQuartzConfig {
    private static final String NAME = "userViewEventSaveJobDetail";
    private static final String GROUP = "user";

    @Value("${app.quartz-config.user-view-event-save.trigger_interval_sec}")
    private int triggerInterval;

    @Bean
    public JobDetail userViewEventSaveJobDetail() {
        return JobBuilder.newJob(UserViewEventSaveJob.class)
                .withIdentity(NAME, GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger userViewEventSaveJobTrigger() {
        SimpleScheduleBuilder scheduler = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(triggerInterval)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(userViewEventSaveJobDetail())
                .withIdentity(NAME, GROUP)
                .withSchedule(scheduler)
                .build();
    }
}
