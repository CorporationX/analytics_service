package faang.school.analytics;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableFeignClients("school.faang.analytics.client")
@EnableKafka
public class AnalyticsServiceApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AnalyticsServiceApp.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
