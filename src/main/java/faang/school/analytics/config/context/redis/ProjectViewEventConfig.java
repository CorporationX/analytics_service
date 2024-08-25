package faang.school.analytics.config.context.redis;

import faang.school.analytics.listener.ProjectMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class ProjectViewEventConfig {

    @Value("${spring.data.redis.project-view}")
    private String redisChannelProjectView;

    @Bean
    public ChannelTopic projectViewTopic() {
        return new ChannelTopic(redisChannelProjectView);
    }

    @Bean
    MessageListenerAdapter projectEventListener(ProjectMessageListener projectMessageConsumer) {
        return new MessageListenerAdapter(projectMessageConsumer);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> projectRequester(MessageListenerAdapter projectEventListener) {
        return Pair.of(projectEventListener, projectViewTopic());
    }
}
