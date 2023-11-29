package by.avorakh.sandbox.spring.web.svc;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import org.jetbrains.annotations.NotNull;

public interface GreetingService {

    @NotNull Greeting hello(@NotNull String name);
}
