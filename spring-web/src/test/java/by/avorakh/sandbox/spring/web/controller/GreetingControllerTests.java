package by.avorakh.sandbox.spring.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.avorakh.sandbox.spring.web.resource.Greeting;
import by.avorakh.sandbox.spring.web.svc.GreetingService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService greetingService;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {
        var defaultName = "World";

        when(greetingService.hello(defaultName)).thenReturn(toGreeting(1, defaultName));

        var requestWithoutNameParam = get("/greeting").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestWithoutNameParam)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content").value(toHelloMessage(defaultName)));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {
        var name = "SomeName";

        when(greetingService.hello(name)).thenReturn(toGreeting(2, name));

        var requestWithNameParam =
                get("/greeting").accept(MediaType.APPLICATION_JSON).param("name", "SomeName");

        mockMvc.perform(requestWithNameParam)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.content").value(toHelloMessage(name)));
    }

    private @NotNull Greeting toGreeting(long id, @NotNull String name) {
        return new Greeting(id, toHelloMessage(name));
    }

    private @NotNull String toHelloMessage(@NotNull String name) {
        return String.format("Hello, %s!", name);
    }
}
