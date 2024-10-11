package faang.school.analytics.config.async.user;

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
    @Value("${app.quartz-config.user-view-event-save.trigger_interval_sec}")
    private int triggerInterval;

    @Bean
    public JobDetail userViewEventSaveJobDetail() {
        return JobBuilder.newJob(UserViewEventSaveJob.class)
                .withIdentity("userViewEventSaveJobDetail", "user")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger userViewEventSaveJobTrigger() {
        SimpleScheduleBuilder cronSchedule = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(triggerInterval)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(userViewEventSaveJobDetail())
                .withIdentity("userViewEventSaveJobDetail", "user")
                .withSchedule(cronSchedule)
                .build();
    }
}
