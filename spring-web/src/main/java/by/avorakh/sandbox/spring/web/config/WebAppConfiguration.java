package by.avorakh.sandbox.spring.web.config;

import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAppConfiguration {
    @Bean
    @NotNull AtomicLong counter() {
        return new AtomicLong();
    }
}
