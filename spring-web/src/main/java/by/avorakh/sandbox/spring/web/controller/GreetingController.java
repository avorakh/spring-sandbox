package by.avorakh.sandbox.spring.web.controller;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GreetingController {

    private static final String MESSAGE_template = "Hello, %s!";
    @NotNull AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public @NotNull Greeting greeting(@RequestParam(value = "name", defaultValue = "World") @NotNull String name) {
        return new Greeting(counter.incrementAndGet(), String.format(MESSAGE_template, name));
    }
}