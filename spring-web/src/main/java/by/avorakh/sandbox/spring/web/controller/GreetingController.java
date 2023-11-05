package by.avorakh.sandbox.spring.web.controller;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import by.avorakh.sandbox.spring.web.svc.GreetingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GreetingController {

    @NotNull GreetingService greetingService;

    @GetMapping("/greeting")
    public @NotNull Greeting greeting(@RequestParam(value = "name", defaultValue = "World") @NotNull String name) {
        return greetingService.hello(name);
    }
}
