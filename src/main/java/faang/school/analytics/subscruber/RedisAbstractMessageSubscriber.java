package faang.school.analytics.subscruber;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class RedisAbstractMessageSubscriber implements MessageListener {

    @Override
    public abstract void onMessage(@NonNull Message message, byte[] pattern);

}
