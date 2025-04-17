package faang.school.analytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Analytics Service API")
                        .description("API для управления Analytics Service")
                        .version("1.0"))
                .addServersItem(new Server()
                        .url("http://localhost:8086")
                        .description("Local Development Server"));
    }
}