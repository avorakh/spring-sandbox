package by.avorakh.sandbox.spring.web.svc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultGreetingServiceTest {

    @Mock
    AtomicLong counter;

    DefaultGreetingService service;

    @BeforeEach
    void setUp() {
        service = new DefaultGreetingService(counter);
    }

    @Test
    void shouldSuccessfullyReturnGreeting() {

        long expectedId = 10L;
        when(counter.incrementAndGet()).thenReturn(expectedId);
        var name = "SomeName";

        var actual = service.hello(name);

        assertNotNull(actual);
        assertEquals(expectedId, actual.id());
        assertEquals(String.format("Hello, %s!", name), actual.content());
    }
}
