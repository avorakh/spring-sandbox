package by.avorakh.sandbox.spring.web.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebAppConfigurationTest {

    WebAppConfiguration config;

    @BeforeEach
    void setUp() {
        config = new WebAppConfiguration();
    }

    @Test
    void shouldSuccessfullyReturnCounter() {
        var actual = config.counter();

        assertNotNull(actual);
    }
}
