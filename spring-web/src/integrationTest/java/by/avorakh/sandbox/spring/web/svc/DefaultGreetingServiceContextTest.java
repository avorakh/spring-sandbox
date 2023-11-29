package by.avorakh.sandbox.spring.web.svc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DefaultGreetingServiceContextTest {

    @Autowired
    GreetingService greetingService;

    @Test
    void contextLoads() throws Exception {
        assertNotNull(greetingService);
    }
}
