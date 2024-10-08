package faang.school.analytics.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
