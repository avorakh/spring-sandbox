package by.avorakh.sandbox.spring.web.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebAppConfigurationContextTest {

    @Autowired
    AtomicLong counter;

    @Test
    void contextLoadsForCounter() throws Exception {
        assertNotNull(counter);
    }
}
