package by.avorakh.sandbox.spring.web.svc;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service("greetingService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultGreetingService implements GreetingService {
    private static final String MESSAGE_template = "Hello, %s!";

    @NotNull AtomicLong counter;

    @Override
    public @NotNull Greeting hello(@NotNull String name) {
        return new Greeting(counter.incrementAndGet(), String.format(MESSAGE_template, name));
    }
}
