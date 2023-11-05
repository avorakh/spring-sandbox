package by.avorakh.sandbox.spring.web.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreetingControllerContextTest {

    @Autowired
    private GreetingController greetingController;

    @Test
    void contextLoads() throws Exception {
        assertNotNull(greetingController);
    }
}